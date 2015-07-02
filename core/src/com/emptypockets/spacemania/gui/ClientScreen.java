package com.emptypockets.spacemania.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Logger;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.commandLine.CommandLinePanel;
import com.emptypockets.spacemania.gui.renderer.EngineRender;
import com.emptypockets.spacemania.gui.tools.StageScreen;
import com.emptypockets.spacemania.input.ClientInputProducer;
import com.emptypockets.spacemania.input.OnScreenInput;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.plotter.DataLogger;

public class ClientScreen extends StageScreen {
	int minTouchSize = 60;
	int insetSize = 50;
	int touchPadSize = 200;

	CommandLinePanel commandLinePanel;
	Touchpad movePad;
	Touchpad shootPad;
	ClientManager client;

	TextButton showConsole;
	boolean alive;

	EngineRender render;

	ClientInputProducer clientInputProducer;

	float playerBoundsBorderX = 0.3f;
	float playerBoundsBorderY = 0.4f;
	Rectangle playerBounds = new Rectangle();
	Vector3 screenLow = new Vector3();
	Vector3 screenHigh = new Vector3();

	public ClientScreen(MainGame mainGame, InputMultiplexer inputMultiplexer) {
		super(mainGame, inputMultiplexer);
		setDrawEvents(true);
		clientInputProducer = new OnScreenInput();
		client = new ClientManager(clientInputProducer);
		setClearColor(Color.BLACK);

		getClient().getCommand().pushHistory("connect 192.168.1.2;login user" + MathUtils.random(100) + ";lobby;");
		// getClient().getCommand().pushHistory("connect 192.168.43.100; login user"
		// + MathUtils.random(100) + ";lobby;");
		// getClient().getCommand().pushHistory("connect 192.168.1.8;login user"
		// + MathUtils.random(100) + ";lobby;");
		getClient().getCommand().pushHistory("start;set grid 1;set gridsize 128 128;set gridrender 1;set roomsize 800;set particles 10000");
		getClient().getCommand().pushHistory("set grid 0;set particles 500");
		getClient().getCommand().pushHistory("start");

	}

	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
	}

	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
	}

	@Override
	public void show() {
		super.show();
		render = new EngineRender();
	}

	@Override
	public void hide() {
		super.hide();
		if (commandLinePanel != null) {
			commandLinePanel.dispose();
		}
		commandLinePanel = null;

		if (client != null) {
			client.dispose();
		}
		client = null;
	}

	@Override
	public void createStage(Stage stage) {
		showConsole = new TextButton("C", getSkin());
		movePad = new Touchpad(0, getSkin());
		shootPad = new Touchpad(0, getSkin());
		((OnScreenInput) clientInputProducer).setMovePad(movePad);
		((OnScreenInput) clientInputProducer).setShootPad(shootPad);
		Pixmap pix = new Pixmap(4, 4, Format.RGBA8888);
		Color c = new Color(Color.LIGHT_GRAY);
		c.a = 0.2f;
		pix.setColor(c);
		pix.fill();
		pix.setColor(Color.LIGHT_GRAY);
		pix.drawRectangle(0, 0, 4, 4);

		NinePatch ninePatch = new NinePatch(new Texture(pix), 1, 1, 1, 1);
		Drawable draw = new NinePatchDrawable(ninePatch);
		movePad.getStyle().background = draw;
		shootPad.getStyle().background = draw;

		Table layout = new Table();
		// top
		layout.row();
		layout.add();
		layout.add().fillX().expandX();
		layout.add();

		// middle
		layout.row();
		layout.add().fillY().expandY();
		layout.add().fill().expand();
		layout.add().fillY().expandY();

		// bottom
		layout.row();
		layout.add(movePad).width(touchPadSize).size(touchPadSize);
		layout.add().fillX().expandX();
		layout.add(shootPad).width(touchPadSize).size(touchPadSize);

		Table inset = new Table();
		inset.row();
		inset.add();
		inset.add().height(insetSize).expandX().fillX();
		inset.add(showConsole).size(minTouchSize, minTouchSize);

		inset.row();
		inset.add().width(insetSize).expandY().fillY();
		inset.add(layout).fill().expand();
		inset.add().width(insetSize).expandY().fillY();

		inset.row();
		inset.add();
		inset.add().height(insetSize).expandX().fillX();
		inset.add();

		inset.setFillParent(true);

		stage.addActor(inset);

		commandLinePanel = new CommandLinePanel(client.getCommand(), minTouchSize);
		stage.addActor(commandLinePanel);
		commandLinePanel.setVisible(false);

		showConsole.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				commandLinePanel.setVisible(!commandLinePanel.isVisible());
				updateCommandScreenSize();
			}
		});
	}

	public ClientManager getClient() {
		return client;
	}

	public void updateCommandScreenSize() {
		commandLinePanel.setPosition(0, 0);
		commandLinePanel.setSize(getStage().getWidth(), getStage().getHeight() - minTouchSize);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		updateCommandScreenSize();
	}

	@Override
	public void initializeRender() {
		super.initializeRender();
		if (client.getPlayer() != null) {
			int myEntityId = client.getPlayer().getEntityId();
			if (client.getEngine() != null) {
				Entity ent = client.getEngine().getEntityManager().getEntityById(myEntityId);
				if (ent != null) {

					/*
					 * Fix player into bounds
					 */
					screenLow.x = getScreenCamera().viewportWidth * (1 - playerBoundsBorderX);
					screenLow.y = getScreenCamera().viewportHeight * (1 - playerBoundsBorderY);
					screenHigh.x = getScreenCamera().viewportWidth * playerBoundsBorderX;
					screenHigh.y = getScreenCamera().viewportHeight * (playerBoundsBorderY);
					getScreenCamera().unproject(screenLow);
					getScreenCamera().unproject(screenHigh);
					playerBounds.set(screenHigh.x, screenHigh.y, screenLow.x - screenHigh.x, screenLow.y - screenHigh.y);
					if (playerBounds.width < 0) {
						playerBounds.x += playerBounds.width;
						playerBounds.width *= -1;
					}
					if (playerBounds.height < 0) {
						playerBounds.y += playerBounds.height;
						playerBounds.height *= -1;
					}

					Vector2 pos = ent.getPos();
					if (!playerBounds.contains(pos)) {
						float x = 0;
						float y = 0;

						if (pos.x < playerBounds.x) {
							x = pos.x - playerBounds.x;
						} else if (pos.x > playerBounds.x + playerBounds.width) {
							x = pos.x - (playerBounds.x + playerBounds.width);
						}

						if (pos.y < playerBounds.y) {
							y = pos.y - playerBounds.y;
						} else if (pos.y > playerBounds.y + playerBounds.height) {
							y = pos.y - (playerBounds.y + playerBounds.height);
						}
						getScreenCamera().translate(x, y);
					}
				}
			}
		}
	}

	@Override
	public void drawScreen(float delta) {
		if (client.getEngine() != null)
			synchronized (client.getEngine()) {
				Gdx.gl.glEnable(GL20.GL_BLEND);
				Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				render.render(getScreenCamera(), client.getEngine());
				Gdx.gl.glDisable(GL20.GL_BLEND);
			}
	}

	@Override
	public void drawOverlay(float delta) {
	}

	@Override
	public void updateLogic(float delta) {
		DataLogger.log("client-logic", 1);
		super.updateLogic(delta);

		if (client.isLoggedIn()) {
			client.sendInput();
		}
		client.update();
	}

	@Override
	public void setupAssetManager(AssetManager assetManager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearAssetManager(AssetManager assetManager) {
		// TODO Auto-generated method stub

	}

}
