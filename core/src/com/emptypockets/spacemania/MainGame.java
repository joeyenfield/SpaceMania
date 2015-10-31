package com.emptypockets.spacemania;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.emptypockets.spacemania.engine.input.NamedInputMultiplexer;
import com.emptypockets.spacemania.gui.tools.GameScreen;
import com.emptypockets.spacemania.gui.tools.Scene2DToolkit;
import com.emptypockets.spacemania.metrics.plotter.DataLogger;
import com.emptypockets.spacemania.screens.GameEngineScreen;
import com.emptypockets.spacemania.screens.LoadingScreen;
import com.emptypockets.spacemania.screens.SplashScreen;

public class MainGame extends Game implements InputProcessor {

	protected AssetManager assetManager;

	protected LoadingScreen loadingScreen;
	protected SplashScreen splashScreen;

	GameScreen currentScreen;

	NamedInputMultiplexer input;

	@Override
	public void create() {
		Scene2DToolkit.getToolkit().reloadSkin();
		input = new NamedInputMultiplexer();
		Gdx.input.setInputProcessor(input);
		input.addProcessor(this, "this");
		assetManager = new AssetManager();
		loadingScreen = new LoadingScreen(this, input);
		splashScreen = new SplashScreen(this, input);

			loadScreen(new GameEngineScreen(this, input), false);

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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		try {
			if (Keys.F1 == keycode) {
				DataLogger.startup();
			} else if (Keys.F2 == keycode) {
				DataLogger.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
