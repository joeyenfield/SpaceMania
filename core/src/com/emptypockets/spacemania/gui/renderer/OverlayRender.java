package com.emptypockets.spacemania.gui.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.network.client.ClientManager;

public class OverlayRender implements Disposable {
	SpriteBatch batch;
	BitmapFont font;
 
	public OverlayRender() {
		batch = new SpriteBatch();
		font = new BitmapFont();
	}

	public void render(Camera camera, ClientManager manager) {
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		float x = -camera.viewportWidth/2;
		float y = camera.viewportHeight/2;
		int glyphHeight = 20;
		font.draw(batch, "FPS : "+String.valueOf(Gdx.graphics.getFramesPerSecond()), x+10, y-glyphHeight);
		if (manager.getPlayer() != null) {
			font.draw(batch, "Deaths : "+String.valueOf(manager.getPlayer().getDeathsCount()), x+10, y-glyphHeight*2);
			font.draw(batch, "Scrap  : "+String.valueOf((int) manager.getPlayer().getScrapCount()), x+10, y-glyphHeight*3);
		}
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
