package com.emptypockets.spacemania.gui.old.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.old.client.player.MyPlayer;
import com.emptypockets.spacemania.network.old.engine.entities.Entity;
import com.emptypockets.spacemania.network.old.engine.entities.bullets.BulletEntity;
import com.emptypockets.spacemania.network.old.engine.entities.players.PlayerEntity;

public class OverlayRender implements Disposable {
	SpriteBatch batch;
	ShapeRenderer shape;
	BitmapFont font;
	

	public OverlayRender() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		shape = new ShapeRenderer();
	}

	public void render(Camera camera, final ClientManager manager) {
		batch.setProjectionMatrix(camera.combined);
		shape.setProjectionMatrix(camera.combined);

		batch.begin();
		final float x = -camera.viewportWidth / 2;
		float topY = camera.viewportHeight / 2;
		int glyphHeight = 20;

		MyPlayer player = manager.getPlayer();
		font.draw(batch, "FPS : " + String.valueOf(Gdx.graphics.getFramesPerSecond()), x + 10, topY - glyphHeight);
		if (player != null) {
			font.draw(batch, "Deaths : " + String.valueOf(player.getDeathsCount()), x + 10, topY - glyphHeight * 2);
			font.draw(batch, "Scrap  : " + String.valueOf((int) player.getScrapCount()), x + 10, topY - glyphHeight * 3);
		}
		batch.end();

		if (manager != null && manager.getEngine() != null && manager.getEngine().getPlayerData() != null && manager.getEngine().getRegion() != null) {
			final float mapX = -50;
			final float mapY = -camera.viewportHeight / 2 + 10;
			shape.begin(ShapeType.Line);
			shape.setColor(Color.WHITE);
			shape.rect(mapX, mapY, 100, 100);
			shape.end();
			shape.begin(ShapeType.Filled);
			manager.getEngine().getEntityManager().process(new SingleProcessor<Entity>() {
				@Override
				public void process(Entity entity) {
					if (entity instanceof BulletEntity) {
						return;
					}
					float pX = (entity.getPos().x - manager.getEngine().getRegion().x) / manager.getEngine().getRegion().width;
					float pY = (entity.getPos().y - manager.getEngine().getRegion().y) / manager.getEngine().getRegion().height;
					shape.setColor(entity.getColor());
					shape.circle(mapX + 100 * pX, mapY + 100 * pY, 1);
				}
			});

			shape.setColor(Color.WHITE);
			manager.getEngine().getPlayerData().process(new SingleProcessor<ClientPlayer>() {

				@Override
				public void process(ClientPlayer entity) {
					PlayerEntity playerEnt = (PlayerEntity) manager.getEngine().getEntityManager().getEntityById(entity.getEntityId());
					if (playerEnt == null) {
						return;
					}
					float pX = (playerEnt.getPos().x - manager.getEngine().getRegion().x) / manager.getEngine().getRegion().width;
					float pY = (playerEnt.getPos().y - manager.getEngine().getRegion().y) / manager.getEngine().getRegion().height;
					shape.circle(mapX + 100 * pX, mapY + 100 * pY, 2);
				}
			});

			shape.end();
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
