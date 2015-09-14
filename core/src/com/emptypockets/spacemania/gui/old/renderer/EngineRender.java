package com.emptypockets.spacemania.gui.old.renderer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.gui.old.renderer.engine.BackgroundRenderer;
import com.emptypockets.spacemania.gui.old.renderer.engine.GridPathRenderer;
import com.emptypockets.spacemania.gui.old.renderer.engine.GridTextureRenderer;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.old.client.ClientEngine;
import com.emptypockets.spacemania.network.old.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.old.client.player.ClientPlayerManager;
import com.emptypockets.spacemania.network.old.engine.EntityManager;
import com.emptypockets.spacemania.network.old.engine.entities.Entity;
import com.emptypockets.spacemania.network.old.engine.entities.bullets.BulletEntity;
import com.emptypockets.spacemania.network.old.engine.entities.collectable.ScoreEntity;
import com.emptypockets.spacemania.network.old.engine.entities.enemy.EnemyEntity;
import com.emptypockets.spacemania.network.old.engine.entities.players.PlayerEntity;
import com.emptypockets.spacemania.network.old.engine.grid.GridSystem;
import com.emptypockets.spacemania.network.old.engine.particles.Particle;
import com.emptypockets.spacemania.network.old.engine.partitioning.cell.Cell;
import com.emptypockets.spacemania.network.old.engine.partitioning.cell.CellSpacePartition;

/**
 * Created by jenfield on 10/05/2015.
 */
public class EngineRender {
	ArrayList<Entity> tempRenderEntitiesHolder = new ArrayList<Entity>();
	ArrayList<Particle> tempRenderParticlesHolder = new ArrayList<Particle>();

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

	GlyphLayout fontLayout = new GlyphLayout();
	BitmapFont font;

	GridTextureRenderer gridTextureRender;
	GridPathRenderer gridPathRender;
	BackgroundRenderer backgroundRender;

	Vector3 screenStart = new Vector3();
	Vector3 screenEnd = new Vector3();
	Rectangle viewport = new Rectangle();

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

	public void renderEntityDebug(OrthographicCamera camera, ArrayList<Entity> entities) {
		if (entities.size() == 0) {
			return;
		}
		shapeRender.begin(ShapeRenderer.ShapeType.Line);
		final Color c = new Color();
		for (Entity entity : entities) {
			c.set(entity.getColor());
			shapeRender.setColor(c);
			shapeRender.circle(entity.getState().getPos().x, entity.getState().getPos().y, entity.getRadius());
		}
		shapeRender.end();
	}

	public void renderEntity(OrthographicCamera camera, ArrayList<Entity> entities, SpriteBatch batch) {
		// Render Players
		batch.begin();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			Entity entity = entities.get(i);
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
		if (Constants.RENDER_TEXTURE) {
			backgroundRender.render(camera, engine);
		}
		/**
		 * Render Grid
		 */
		if (Constants.RENDER_DEBUG) {
			renderSpatialPartionDebug(camera, engine.getEntitySpatialPartition());
		}
		if (Constants.RENDER_TEXTURE && engine.getGridData().getRenderType() == GridSystem.RENDER_PATH) {
			engine.getGridData().addListener(gridPathRender);
			engine.getGridData().removeListener(gridTextureRender);
			gridPathRender.updateBounds();
			gridPathRender.render(engine.getGridData(), viewport, shapeRender);
		} else if (Constants.RENDER_TEXTURE && engine.getGridData().getRenderType() == GridSystem.RENDER_TEXTURE) {
			engine.getGridData().addListener(gridTextureRender);
			engine.getGridData().removeListener(gridPathRender);
			// Render Grid
			gridTextureRender.render(camera, engine);
		} else {
			shapeRender.begin(ShapeType.Line);
			shapeRender.setColor(Color.WHITE);
			shapeRender.rect(engine.getRegion().x, engine.getRegion().y, engine.getRegion().width, engine.getRegion().height);
			shapeRender.end();
		}

		/**
		 * Render Particles
		 */

		if (Constants.RENDER_TEXTURE) {
			tempRenderParticlesHolder.clear();
			engine.getParticleSystem().getEntities(viewport, tempRenderParticlesHolder);
			renderParticles(tempRenderParticlesHolder, spriteBatch);
		}
		/**
		 * Render Entities
		 */
		tempRenderEntitiesHolder.clear();
		engine.getEntitySpatialPartition().getEntities(viewport, tempRenderEntitiesHolder);

		if (Constants.RENDER_TEXTURE) {
			renderEntity(camera, tempRenderEntitiesHolder, spriteBatch);
		}

		if (Constants.RENDER_DEBUG) {
			renderEntityDebug(camera, tempRenderEntitiesHolder);
		}
		
		renderPlayerNames(engine.getPlayerData(), engine.getEntityManager());
	}

	public void renderPlayerNames(ClientPlayerManager players, final EntityManager entities) {

		spriteBatch.begin();
		players.process(new SingleProcessor<ClientPlayer>() {
			@Override
			public void process(ClientPlayer entity) {
				Entity ent = entities.getEntityById(entity.getEntityId());
				if (ent != null) {
					Vector2 pos = ent.getPos();
					
					//Draw Username
					fontLayout.setText(font, entity.getUsername());
					font.draw(spriteBatch, fontLayout, pos.x-fontLayout.width/2, pos.y-(ent.getRadius()+fontLayout.height));
					
					fontLayout.setText(font, String.valueOf(entity.getPing()));
					font.draw(spriteBatch, fontLayout, pos.x-fontLayout.width/2, pos.y-(ent.getRadius()+2.5f*fontLayout.height));

				}
			}
		});
		spriteBatch.end();
	}

	public void renderParticles(ArrayList<Particle> particles, final SpriteBatch batch) {
		batch.begin();
		int size = particles.size();
		for (int i = 0; i < size; i++) {
			Particle entity = particles.get(i);
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