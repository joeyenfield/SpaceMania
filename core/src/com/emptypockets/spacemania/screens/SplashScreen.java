package com.emptypockets.spacemania.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.gui.tools.GameScreen;

public class SplashScreen extends GameScreen{

	long startTime = 0;
	String splashScreenFile = "splash/logo.png";
	Texture texture;
	SpriteBatch batch;
	
	public SplashScreen(MainGame mainGame, InputMultiplexer inputProcessor) {
		super(mainGame, inputProcessor);
	}

	@Override
	public void show() {
		super.show();
		startTime= System.currentTimeMillis();
		batch = new SpriteBatch();
		
		texture = mainGame.getAssetManager().get(splashScreenFile);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	@Override
	public void hide() {
		super.hide();
		
		texture.dispose();
		batch.dispose();
		
		texture = null;
		batch = null;
	}

	@Override
	public void drawScreen(float delta) {
		batch.begin();
		batch.draw(texture, 0,0);
		batch.end();
	}

	@Override
	public void drawOverlay(float delta) {
		
	}
	
	@Override
	public boolean tap(float x, float y, int count, int button) {
		return super.tap(x, y, count, button);
	}

	@Override
	public void setupAssetManager(AssetManager assetManager) {
		assetManager.load(splashScreenFile, Texture.class);
	}

	@Override
	public void clearAssetManager(AssetManager assetManager) {
		assetManager.unload(splashScreenFile);
	}

}
