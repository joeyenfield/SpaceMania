package com.emptypockets.spacemania;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.emptypockets.spacemania.gui.GameEngineScreen;
import com.emptypockets.spacemania.gui.old.ClientScreen;
import com.emptypockets.spacemania.gui.screens.LoadingScreen;
import com.emptypockets.spacemania.gui.screens.SplashScreen;
import com.emptypockets.spacemania.gui.tools.GameScreen;
import com.emptypockets.spacemania.gui.tools.Scene2DToolkit;

public class MainGame extends Game {

	protected AssetManager assetManager;

	protected LoadingScreen loadingScreen;
	protected SplashScreen splashScreen;
	public ClientScreen screen;

	GameScreen currentScreen;

	InputMultiplexer input;

	@Override
	public void create() {
		Scene2DToolkit.getToolkit().reloadSkin();
		input = new InputMultiplexer();
		Gdx.input.setInputProcessor(input);
		assetManager = new AssetManager();
		loadingScreen = new LoadingScreen(this, input);
		splashScreen = new SplashScreen(this, input);

		boolean legacyGame = false;

		if (legacyGame) {
			screen = new ClientScreen(this, input);
			loadScreen(screen, false);
		} else {
			loadScreen(new GameEngineScreen(this, input), false);
		}

	}

	public void loadScreen(GameScreen gameScreen, boolean proceededOnLood) {
		if (currentScreen != null) {
			currentScreen.clearAssetManager(assetManager);
		}
		currentScreen = gameScreen;
		loadingScreen.setProceededOnLood(proceededOnLood);
		loadingScreen.setNextScreen(currentScreen);
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		Gdx.input.setInputProcessor(null);
		setScreen(null);
		if (screen != null) {
			screen.dispose();
		}
		screen = null;
		Scene2DToolkit.getToolkit().disposeSkin();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void pause() {
		super.pause();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}
