package com.emptypockets.spacemania.gui.old;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.commandLine.CommandLinePanel;
import com.emptypockets.spacemania.engine.input.NamedInputMultiplexer;
import com.emptypockets.spacemania.gui.old.renderer.EngineRender;
import com.emptypockets.spacemania.gui.old.renderer.OverlayRender;
import com.emptypockets.spacemania.gui.tools.StageScreen;
import com.emptypockets.spacemania.metrics.plotter.DataLogger;
import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.rooms.ClientSpawnCommand;
import com.emptypockets.spacemania.network.old.engine.entities.Entity;
import com.emptypockets.spacemania.network.old.input.ClientInputProducer;
import com.emptypockets.spacemania.network.old.input.OnScreenInput;
import com.emptypockets.spacemania.network.old.server.player.ServerPlayer;

public class ClientScreen extends StageScreen {
	int minTouchSize = 60;
	int insetSize = 50;
	int touchPadSize = 200;
	long lastInputSentToServer = 0;

	CommandLinePanel commandLinePanel;
	Touchpad movePad;
	Touchpad shootPad;
	ClientManager client;

	TextButton showConsole;

	TextButton respawnButton;

	boolean alive;

	EngineRender render;
	OverlayRender overlay;

	ClientInputProducer clientInputProducer;

	float playerBoundsBorderX = 0.3f;
	float playerBoundsBorderY = 0.4f;
	Rectangle playerBounds = new Rectangle();
	Vector3 playerPositionFixTemp = new Vector3();
	Vector3 playerOffsetFixTemp = new Vector3();

	public ClientScreen(MainGame mainGame, NamedInputMultiplexer inputMultiplexer) {
		super(mainGame, inputMultiplexer);
		setDrawEvents(false);
		clientInputProducer = new OnScreenInput();
		client = new ClientManager(clientInputProducer);
		setClearColor(Color.BLACK);

		getClient().getCommand().pushHistory("connect emptypocketgames.noip.me;login user" + MathUtils.random(100) + ";lobby");
		// getClient().getCommand().pushHistory("connect 192.168.43.100; login user"
		// + MathUtils.random(100) + ";lobby;");
		// getClient().getCommand().pushHistory("connect 192.168.1.8;login user"
		// + MathUtils.random(100) + ";lobby;");
		getClient().getCommand().pushHistory("start;set grid 1;set gridsize 128 128;set gridrender 0;set roomsize 2000;set particles 1000");
		getClient().getCommand().pushHistory("start;set grid 1;set gridsize 2 2;set gridrender 0;set roomsize 2000;set particles 1000");
		getClient().getCommand().pushHistory("set grid 0;set particles 500");
		getClient().getCommand().pushHistory("start");

	}

	@Override
	public void addInputMultiplexer(NamedInputMultiplexer input) {
		super.addInputMultiplexer(input);
	}

	@Override
	public void removeInputMultiplexer(NamedInputMultiplexer input) {
		super.removeInputMultiplexer(input);
	}

	@Override
	public void show() {
		super.show();
		render = new EngineRender();
		overlay = new OverlayRender();
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
		respawnButton = new TextButton("Respawn", getSkin());

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
		inset.add(respawnButton).height(insetSize).expandX().fillX();
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

		respawnButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				getClient().getCommand().processCommand(ClientSpawnCommand.COMMAND_TEXT);
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
				if (ent == null) {
				} else {
					/*
					 * Fix player into bounds
					 */
					playerPositionFixTemp.x = (ent.getPos().x);
					playerPositionFixTemp.y = (ent.getPos().y);

					// Map Player into screen
					screenCamera.project(playerPositionFixTemp);

					// Normalise
					playerPositionFixTemp.x /= Gdx.graphics.getWidth();
					playerPositionFixTemp.y /= Gdx.graphics.getHeight();

					if (playerPositionFixTemp.x < playerBoundsBorderX) {
						// Move Left
						playerOffsetFixTemp.x = playerPositionFixTemp.x - playerBoundsBorderX;
					} else if (playerPositionFixTemp.x > (1 - playerBoundsBorderX)) {
						// Move Right
						playerOffsetFixTemp.x = playerPositionFixTemp.x - (1 - playerBoundsBorderX);
					} else {
						playerOffsetFixTemp.x = 0;
					}

					if (playerPositionFixTemp.y < playerBoundsBorderY) {
						// Move Left
						playerOffsetFixTemp.y = playerPositionFixTemp.y - playerBoundsBorderY;
					} else if (playerPositionFixTemp.y > (1 - playerBoundsBorderY)) {
						// Move Right
						playerOffsetFixTemp.y = playerPositionFixTemp.y - (1 - playerBoundsBorderY);
					} else {
						playerOffsetFixTemp.y = 0;
					}

					// Denormalise
					playerOffsetFixTemp.x *= Gdx.graphics.getWidth();
					playerOffsetFixTemp.y *= Gdx.graphics.getHeight();
					screenCamera.unproject(playerOffsetFixTemp);

					// Get Distance
					playerPositionFixTemp.x = 0;
					playerPositionFixTemp.y = 0;
					screenCamera.unproject(playerPositionFixTemp);
					screenCamera.translate(playerOffsetFixTemp.x - playerPositionFixTemp.x, -(playerOffsetFixTemp.y - playerPositionFixTemp.y));
				}
			}
		}
	}

	@Override
	public void drawScreen(float delta) {
		if (client.getEngine() != null)
			synchronized (client.getEngine()) {
				render.render(getScreenCamera(), client.getEngine());
			}
	}

	@Override
	public void drawOverlay(float delta) {
		overlay.render(getOverlayCamera(), client);
	}

	@Override
	public void updateLogic(float delta) {
		if (DataLogger.isEnabled()) {
			DataLogger.log("client-logic", 1);
		}
		super.updateLogic(delta);

		boolean playerAlive = true;
		if (getClient() != null && getClient().getPlayer() != null) {
			playerAlive = getClient().getPlayer().getEntityId() != ServerPlayer.NO_ENTITY;
		}
		respawnButton.setDisabled(playerAlive);
		respawnButton.setVisible(!playerAlive);
		if (client.isLoggedIn()) {
			if (System.currentTimeMillis() - lastInputSentToServer > Constants.CLIENT_TIME_INPUT_TO_SERVER_PEROID) {
				lastInputSentToServer = System.currentTimeMillis();
				client.sendInput();
			}
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
