package com.emptypockets.spacemania.gui.renderer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.engine.EntityManager;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.engine.grid.GridSystem;
import com.emptypockets.spacemania.network.engine.partitioning.cell.Cell;
import com.emptypockets.spacemania.network.engine.partitioning.cell.CellSpacePartition;

/**
 * Created by jenfield on 10/05/2015.
 */
public class EngineRender {
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRender;

	Texture playerShip;
	Sprite playerSprite;

	Texture bullet;
	Sprite bulletSprite;

	BitmapFont font;

	boolean debugEnabled = true;

	GridTextureRenderer gridTextureRender;
	GridPathRenderer gridPathRender;
	ParticleSystemRenderer particleRender;

	Vector3 screenStart = new Vector3();
	Vector3 screenEnd = new Vector3();
	Rectangle viewport = new Rectangle();

	boolean lastGridTextureRender = false;

	public EngineRender() {
		gridTextureRender = new GridTextureRenderer();
		gridPathRender = new GridPathRenderer();
		particleRender = new ParticleSystemRenderer();
		shapeRender = new ShapeRenderer();
		spriteBatch = new SpriteBatch();

		playerShip = new Texture("playership.png");
		playerSprite = new Sprite(playerShip);
		playerSprite.setOriginCenter();

		bullet = new Texture("bullet.png");
		bulletSprite = new Sprite(bullet);

		playerSprite.setOriginCenter();
		bulletSprite.setOriginCenter();

		font = new BitmapFont();
	}

	public void renderSpatialPartionDebug(OrthographicCamera camera, CellSpacePartition partition) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFuncSeparate(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA, Gdx.gl20.GL_ONE, Gdx.gl20.GL_ONE);

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

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFuncSeparate(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA, Gdx.gl20.GL_ONE, Gdx.gl20.GL_ONE);

		shapeRender.begin(ShapeRenderer.ShapeType.Filled);
		final Color c = new Color();
		for (Entity entity : entities) {
			if (!(entity instanceof PlayerEntity) && !(entity instanceof BulletEntity)) {
				c.set(entity.getColor());
				shapeRender.setColor(c);
				shapeRender.circle(entity.getState().getPos().x, entity.getState().getPos().y, entity.getRadius());
			}
		}
		shapeRender.end();
	}

	public void renderEntity(OrthographicCamera camera, ArrayList<Entity> entities) {
		// Render Players
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		for (Entity entity : entities) {
			if (entity.isAlive()) {
				if ((entity instanceof PlayerEntity)) {
					playerSprite.setColor(entity.getColor());
					playerSprite.setPosition(entity.getPos().x - entity.getRadius(), entity.getPos().y - entity.getRadius());
					playerSprite.setOriginCenter();
					playerSprite.setRotation(entity.getState().getAng());
					playerSprite.setSize(2 * entity.getRadius(), 2 * entity.getRadius());
					playerSprite.draw(spriteBatch);
				}
				if (entity instanceof BulletEntity) {
					bulletSprite.setColor(entity.getColor());
					bulletSprite.setPosition(entity.getPos().x - entity.getRadius(), entity.getPos().y - entity.getRadius());
					bulletSprite.setOriginCenter();
					bulletSprite.setRotation(entity.getState().getAng());
					bulletSprite.setSize(2 * entity.getRadius(), 2 * entity.getRadius());
					bulletSprite.draw(spriteBatch);
				}
			}
		}

		spriteBatch.end();
	}

	boolean firstPathRender = true;

	public void render(OrthographicCamera camera, ClientEngine engine) {
		shapeRender.setProjectionMatrix(camera.combined);
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

		// Background
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
		particleRender.render(engine.getParticleSystem(), viewport, spriteBatch);

		ArrayList<Entity> renderEntities = new ArrayList<Entity>();
		engine.getEntitySpatialPartition().getEntities(viewport, renderEntities);

		// Entities
		renderEntityDebug(camera, renderEntities);
		renderEntity(camera, renderEntities);
	}
}
