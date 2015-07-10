package com.emptypockets.spacemania.gui.renderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.EnemyEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.engine.entities.collect.ScoreEntity;
import com.emptypockets.spacemania.network.engine.grid.GridSystem;
import com.emptypockets.spacemania.network.engine.particles.Particle;
import com.emptypockets.spacemania.network.engine.particles.ParticleSystem;
import com.emptypockets.spacemania.network.engine.partitioning.cell.Cell;
import com.emptypockets.spacemania.network.engine.partitioning.cell.CellSpacePartition;

/**
 * Created by jenfield on 10/05/2015.
 */
public class EngineRender {
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRender;

	TextureAtlas textureAtlas;
	AtlasRegion playerRegion;
	AtlasRegion enemyFollowRegion;
	AtlasRegion enemyRandomRegion;
	AtlasRegion bulletRegion;
	AtlasRegion scoreRegion;
	AtlasRegion defaultRegion;

	AtlasRegion sparkRegion;
	AtlasRegion smokeRegion;
	AtlasRegion debrisRegion;

	Affine2 transform = new Affine2();

	BitmapFont font;

	boolean debugEnabled = true;

	GridTextureRenderer gridTextureRender;
	GridPathRenderer gridPathRender;
	BackgroundRenderer backgroundRender;

	Vector3 screenStart = new Vector3();
	Vector3 screenEnd = new Vector3();
	Rectangle viewport = new Rectangle();

	boolean lastGridTextureRender = false;

	public EngineRender() {
		textureAtlas = new TextureAtlas("game/game.atlas");

		for (Texture t : textureAtlas.getTextures()) {
			t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}

		gridTextureRender = new GridTextureRenderer();
		gridPathRender = new GridPathRenderer();
		shapeRender = new ShapeRenderer();
		backgroundRender = new BackgroundRenderer();

		spriteBatch = new SpriteBatch();

		int src = GL20.GL_SRC_ALPHA;
		int dst = GL20.GL_ONE;
		spriteBatch.setBlendFunction(src, dst);

		playerRegion = textureAtlas.findRegion("playership");
		enemyFollowRegion = textureAtlas.findRegion("enemy-follow");
		enemyRandomRegion = textureAtlas.findRegion("enemy-random");
		bulletRegion = textureAtlas.findRegion("bullet");
		sparkRegion = textureAtlas.findRegion("spark");
		smokeRegion = textureAtlas.findRegion("smoke");
		debrisRegion = textureAtlas.findRegion("debris");

		scoreRegion = textureAtlas.findRegion("score");
		defaultRegion = textureAtlas.findRegion("default");

		font = new BitmapFont();
	}

	public void renderSpatialPartionDebug(OrthographicCamera camera, CellSpacePartition partition) {
		shapeRender.begin(ShapeRenderer.ShapeType.Filled);
		Cell[][] cells = partition.getCells();
		Color c = new Color();
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells.length; y++) {
				Cell cell = cells[x][y];
				if (cell.getEntities().size() > 0) {
					c.set(Color.RED).a = 0.1f;
				} else {
					c.set(Color.GREEN).a = 0.1f;
				}
				shapeRender.setColor(c);
				Rectangle rec = cell.getBounds();
				shapeRender.rect(rec.x, rec.y, rec.width, rec.height);
			}
		}
		shapeRender.end();

	}

	public void renderEntityDebug(OrthographicCamera camera, Set<Entity> entities) {
		if (entities.size() == 0) {
			return;
		}
		shapeRender.begin(ShapeRenderer.ShapeType.Line);
		final Color c = new Color();
		for (Entity entity : entities) {
			c.set(entity.getColor());
			shapeRender.setColor(c);
			shapeRender.circle(entity.getState().getPos().x, entity.getState().getPos().y, entity.getRadius());
			if (entity instanceof PlayerEntity) {
				shapeRender.circle(entity.getState().getPos().x, entity.getState().getPos().y, entity.getRadius() + ((PlayerEntity) entity).getMagnetDistance());
			}
		}
		shapeRender.end();
	}

	public void renderEntity(OrthographicCamera camera, Set<Entity> entities, SpriteBatch batch) {
		// Render Players
		batch.begin();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		for (Entity entity : entities) {
			if (entity.isAlive()) {
				transform.idt();
				transform.translate(entity.getPos());
				transform.rotate(entity.getState().getAng());
				transform.translate(-entity.getRadius(), -entity.getRadius());

				batch.setColor(entity.getColor());
				AtlasRegion region = defaultRegion;
				if ((entity instanceof PlayerEntity)) {
					batch.setColor(Color.WHITE);
					region = playerRegion;
				} else if (entity instanceof BulletEntity) {
					region = bulletRegion;
				} else if (entity instanceof EnemyEntity) {
					switch (((EnemyEntity) entity).getType()) {
					case Enemy_FOLLOW:
						region = enemyFollowRegion;
						break;
					case Enemy_RANDOM:
						region = enemyRandomRegion;
						break;
					default:
						break;
					}
				} else if (entity instanceof ScoreEntity) {
					region = scoreRegion;
				}
				batch.draw(region, entity.getRadius() * 2, entity.getRadius() * 2, transform);
			}
		}
		batch.end();

	}

	boolean firstPathRender = true;

	public void render(OrthographicCamera camera, ClientEngine engine) {
		shapeRender.setProjectionMatrix(camera.combined);

		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.setProjectionMatrix(camera.combined);

		screenStart.set(0, 0, 0);
		screenEnd.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
		camera.unproject(screenStart);
		camera.unproject(screenEnd);
		viewport.set(screenStart.x, screenStart.y, (screenEnd.x - screenStart.x), (screenEnd.y - screenStart.y));

		if (viewport.width < 0) {
			viewport.x += viewport.width;
			viewport.width *= -1;
		}
		if (viewport.height < 0) {
			viewport.y += viewport.height;
			viewport.height *= -1;
		}

		/**
		 * Render Background
		 */
		backgroundRender.render(camera, engine);

		/**
		 * Render Grid
		 */
		// renderSpatialPartionDebug(camera, engine.getEntitySpatialPartition());
		if (engine.getGridData().getRenderType() == GridSystem.RENDER_PATH) {
			engine.getGridData().addListener(gridPathRender);
			engine.getGridData().removeListener(gridTextureRender);
			gridPathRender.updateBounds();
			gridPathRender.render(engine.getGridData(), viewport, shapeRender);
		} else if (engine.getGridData().getRenderType() == GridSystem.RENDER_TEXTURE) {
			engine.getGridData().addListener(gridTextureRender);
			engine.getGridData().removeListener(gridPathRender);
			// Render Grid
			gridTextureRender.render(camera, engine);
			lastGridTextureRender = true;
		} else {
			shapeRender.begin(ShapeType.Line);
			shapeRender.setColor(Color.WHITE);
			shapeRender.rect(engine.getRegion().x, engine.getRegion().y, engine.getRegion().width, engine.getRegion().height);
			shapeRender.end();
		}

		/**
		 * Render Particles
		 */
		Set<Particle> particles = new HashSet<Particle>();
		engine.getParticleSystem().getEntities(viewport, particles);
		renderParticles(particles, spriteBatch);

		/**
		 * Render Entities
		 */
		Set<Entity> renderEntities = new HashSet<Entity>();
		engine.getEntitySpatialPartition().getEntities(viewport, renderEntities);
		// renderEntityDebug(camera, renderEntities);
		renderEntity(camera, renderEntities, spriteBatch);

		// renderEntities.clear();
		// if (ServerManager.manager != null) {
		// ServerManager manager = ServerManager.manager;
		// manager.getLobbyRoom().getEngine().getEntitySpatialPartition().getEntities(viewport,
		// renderEntities);
		// engine.getEntitySpatialPartition().getEntities(viewport,
		// renderEntities);
		// renderEntity(camera, renderEntities, batch);
		// }

	}

	public void renderParticles(Set<Particle> particles, final SpriteBatch batch) {
		batch.begin();
		for (Particle entity : particles) {
			if (!entity.isDead()) {

				AtlasRegion region = sparkRegion;
				float sizeX = 1;
				float sizeY = 1;

				switch (entity.getType()) {
				case DEBRIS:
					region = debrisRegion;
					break;
				case SMOKE:
					region = smokeRegion;
					break;
				case SPARK:
					region = sparkRegion;
					break;
				default:
					region = sparkRegion;
					break;
				}

				sizeX = region.getRegionWidth() * entity.getCurrentScaleX();
				sizeY = region.getRegionHeight() * entity.getCurrentScaleY();

				transform.idt();
				transform.translate(entity.getPos());
				transform.rotate(entity.getAngle());
				transform.translate(-sizeX / 2, -sizeY / 2);
				batch.setColor(entity.getCurrentColor());
				batch.draw(region, sizeX, sizeY, transform);
			}
		}
		batch.end();
	}
}
