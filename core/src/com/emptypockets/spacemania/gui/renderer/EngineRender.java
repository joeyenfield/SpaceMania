package com.emptypockets.spacemania.gui.renderer;

import java.util.ArrayList;

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
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.EnemyEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
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
	SpriteBatch entityBatch;
	SpriteBatch particleBatch;
	SpriteBatch backgroundBatch;

	ShapeRenderer shapeRender;

	TextureAtlas textureAtlas;
	AtlasRegion playerRegion;
	AtlasRegion followRegion;
	AtlasRegion bulletRegion;
	AtlasRegion sparkRegion;
	AtlasRegion scoreRegion;
	AtlasRegion defaultRegion;

	Affine2 transform = new Affine2();

	BitmapFont font;

	boolean debugEnabled = true;

	GridTextureRenderer gridTextureRender;
	GridPathRenderer gridPathRender;

	Vector3 screenStart = new Vector3();
	Vector3 screenEnd = new Vector3();
	Rectangle viewport = new Rectangle();

	boolean lastGridTextureRender = false;

	public EngineRender() {
		textureAtlas = new TextureAtlas("game/game.atlas");
		for (Texture t : textureAtlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		gridTextureRender = new GridTextureRenderer();
		gridPathRender = new GridPathRenderer();
		shapeRender = new ShapeRenderer();

		entityBatch = new SpriteBatch();
		backgroundBatch = new SpriteBatch();
		particleBatch = new SpriteBatch();
//
//		particleBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
//		entityBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
//		backgroundBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		playerRegion = textureAtlas.findRegion("playership");
		followRegion = textureAtlas.findRegion("follow");
		bulletRegion = textureAtlas.findRegion("bullet");
		sparkRegion = textureAtlas.findRegion("spark");
		scoreRegion = textureAtlas.findRegion("score");
		defaultRegion = textureAtlas.findRegion("default");

		font = new BitmapFont();
	}

	public void initRender() {

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

	public void renderEntityDebug(OrthographicCamera camera, ArrayList<Entity> entities) {
		if (entities.size() == 0) {
			return;
		}
		shapeRender.begin(ShapeRenderer.ShapeType.Line);
		final Color c = new Color();
		for (Entity entity : entities) {
			// if (!(entity instanceof PlayerEntity) && !(entity instanceof
			// BulletEntity) && !(entity.getType() == EntityType.Enemy_FOLLOW))
			// {
			c.set(entity.getColor());
			shapeRender.setColor(c);
			shapeRender.circle(entity.getState().getPos().x, entity.getState().getPos().y, entity.getRadius());
			// }
		}
		shapeRender.end();
	}

	public void renderEntity(OrthographicCamera camera, ArrayList<Entity> entities, SpriteBatch batch) {
		// Render Players
		batch.begin();
		for (Entity entity : entities) {
			if (entity.isAlive()) {
				transform.idt();
				transform.translate(entity.getPos());
				transform.rotate(entity.getState().getAng());
				transform.translate(-entity.getRadius(), -entity.getRadius());

				batch.setColor(entity.getColor());
				AtlasRegion region = defaultRegion;
				if ((entity instanceof PlayerEntity)) {
					region = playerRegion;
				} else if (entity instanceof BulletEntity) {
					region = bulletRegion;
				} else if (entity instanceof EnemyEntity) {
					switch (((EnemyEntity) entity).getType()) {
					case Enemy_FOLLOW:
						region = followRegion;
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

		entityBatch.setProjectionMatrix(camera.combined);
		particleBatch.setProjectionMatrix(camera.combined);
		backgroundBatch.setProjectionMatrix(camera.combined);

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

		// renderSpatialPartionDebug(camera,
		// engine.getEntitySpatialPartition());
		if (engine.getGridData().getRenderType() == GridSystem.RENDER_PATH) {
			if (firstPathRender || engine.isRegionChanged()) {
				gridPathRender.rebuild(engine.getGridData());
				firstPathRender = false;
			}
			gridPathRender.updateBounds();
			gridPathRender.render(engine.getGridData(), viewport, shapeRender);
		} else {
			if (engine.isRegionChanged() || !lastGridTextureRender) {
				gridTextureRender.relayoutMesh();
			}
			// Render Grid
			gridTextureRender.render(camera, engine);
			lastGridTextureRender = true;
		}

		// Particles
		renderParticles(engine.getParticleSystem(), viewport, particleBatch);

		// Entities
		ArrayList<Entity> renderEntities = new ArrayList<Entity>();
		engine.getEntitySpatialPartition().getEntities(viewport, renderEntities);
//		renderEntityDebug(camera, renderEntities);
		renderEntity(camera, renderEntities, entityBatch);
	}

	public void renderParticles(ParticleSystem particleSystem, final Rectangle viewport, final SpriteBatch batch) {
		batch.begin();
		particleSystem.process(new SingleProcessor<Particle>() {
			@Override
			public void process(Particle entity) {
				if (entity.isDead() || !viewport.contains(entity.getPos())) {
					return;
				}
				transform.idt();
				transform.translate(entity.getPos());
				transform.rotate(entity.getVel().angle());
				transform.translate(-entity.getRadius(), -entity.getRadius());
				batch.setColor(entity.getCurrentColor());
				float velScale = .05f;
				batch.draw(sparkRegion, entity.getVel().len() * velScale, 3, transform);
			}
		});
		batch.end();
	}
}
