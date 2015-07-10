package com.emptypockets.spacemania.gui.renderer;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.network.client.ClientEngine;

public class BackgroundRenderer {
	Texture starfieldDeepTexture;
	Texture starfieldParalaxTexture;
	Rectangle world = new Rectangle(-1e6f, -1e6f, 2e6f, 2e6f);

	SpriteBatch batch;

	public BackgroundRenderer() {
		init();
	}

	private void createShader() {
		starfieldDeepTexture = new Texture("background.png");
		starfieldParalaxTexture = new Texture("starfield-b-2048.png");
		batch = new SpriteBatch();

		starfieldDeepTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		starfieldParalaxTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		starfieldDeepTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		starfieldParalaxTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	public void init() {
		createShader();
	}

	public void render(OrthographicCamera camera, ClientEngine engine) {
		batch.setProjectionMatrix(camera.combined);
		float offsetX = 0.1f;
		float offsetY = 0.1f;

		batch.begin();
		batch.disableBlending();
		batch.setColor(1, 1, 1, 1);

		batch.draw(starfieldDeepTexture, 
				world.x, world.y, world.width, world.height, 
				offsetX, 
				offsetY, 
				offsetX + world.width / (starfieldDeepTexture.getWidth()), 
				offsetY + world.height / (starfieldDeepTexture.getHeight())
						);
		batch.enableBlending();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		float paralaxScale = 50;
		float fx = ((camera.position.x / starfieldDeepTexture.getWidth()) / paralaxScale);
		float fy = ((camera.position.y / starfieldDeepTexture.getHeight()) / paralaxScale);

		float[][] offsets = new float[][] { { 0, 0 }, { 0.3f, 0.5f }};//, { 0.4f, 0.8f }, { 0.6f, 0.9f }, { 0.2f, 0.3f } };
		for (int i = 0; i < offsets.length; i++) {
			offsetX = fx/(i+1)+offsets[i][0];
			offsetY = fy/(i+1)+offsets[i][1];
			batch.draw(starfieldParalaxTexture, world.x, world.y, world.width, world.height, offsetX, offsetY, offsetX + world.width / starfieldParalaxTexture.getWidth(), offsetY + world.height / starfieldParalaxTexture.getHeight());
		}
		// offsetX += fx+.5f;
		// offsetY += fy+.3f;
		// batch.setColor(1, 1, 1, 1);
		// batch.draw(starfieldParalaxTexture, world.x, world.y, world.width, world.height, offsetX, offsetY, offsetX + world.width / starfieldParalaxTexture.getWidth(), offsetY + world.height / starfieldParalaxTexture.getHeight());

		batch.end();
	}

}
