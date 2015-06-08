package com.emptypockets.spacemania.gui.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.engine.EntityManager;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;

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

	boolean debugEnabled = true;

	GridTextureRenderer gridTextureRender;
	GridPathRenderer gridPathRender;
	ParticleSystemRenderer particleRender;
	
	Vector3 screenStart = new Vector3();
	Vector3 screenEnd = new Vector3();
	Rectangle viewport = new Rectangle();

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

	}

	public void renderEntityDebug(OrthographicCamera camera, EntityManager manager) {
		if (manager == null) {
			return;
		}
		shapeRender.begin(ShapeRenderer.ShapeType.Filled);
		manager.process(new SingleProcessor<Entity>() {
			@Override
			public void process(Entity entity) {
				if (!entity.isAlive() || (entity instanceof PlayerEntity) || (entity instanceof BulletEntity)) {
					return;
				}
				shapeRender.setColor(entity.getColor());
				shapeRender.circle(entity.getState().getPos().x, entity.getState().getPos().y, entity.getRadius());
			}
		});
		shapeRender.end();

		// Render Players
		spriteBatch.begin();
		spriteBatch.enableBlending();
		spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		manager.process(new SingleProcessor<Entity>() {
			@Override
			public void process(Entity entity) {
				if (!entity.isAlive() || !(entity instanceof PlayerEntity)) {
					return;
				}
				playerSprite.setColor(entity.getColor());
				playerSprite.setPosition(entity.getPos().x - entity.getRadius(), entity.getPos().y - entity.getRadius());
				playerSprite.setOriginCenter();
				playerSprite.setRotation(entity.getState().getAng());
				playerSprite.setSize(2 * entity.getRadius(), 2 * entity.getRadius());
				playerSprite.draw(spriteBatch);
			}
		});

		manager.process(new SingleProcessor<Entity>() {
			@Override
			public void process(Entity entity) {
				if (!entity.isAlive() || !(entity instanceof BulletEntity)) {
					return;
				}
				bulletSprite.setColor(entity.getColor());
				bulletSprite.setPosition(entity.getPos().x - entity.getRadius(), entity.getPos().y - entity.getRadius());
				bulletSprite.setOriginCenter();
				bulletSprite.setRotation(entity.getState().getAng());
				bulletSprite.setSize(2 * entity.getRadius(), 2 * entity.getRadius());
				bulletSprite.draw(spriteBatch);
			}
		});

		spriteBatch.end();
	}

	boolean first = true;
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
		
		if(first){
			gridPathRender.rebuild(engine.getGridData());
			first = false;
		}
		
		gridPathRender.updateBounds();
		gridPathRender.render(engine.getGridData(), viewport, shapeRender);

		// Render Grid
//		gridTextureRender.render(camera, engine);

		// Particles
		particleRender.render(engine.getParticleSystem(),viewport, spriteBatch);

		// Entities
		renderEntityDebug(camera, engine.getEntityManager());
	}
}
