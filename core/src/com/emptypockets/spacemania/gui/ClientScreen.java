package com.emptypockets.spacemania.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.commandLine.CommandLinePanel;
import com.emptypockets.spacemania.gui.renderer.EntityRender;
import com.emptypockets.spacemania.gui.tools.StageScreen;
import com.emptypockets.spacemania.input.ClientInputProducer;
import com.emptypockets.spacemania.input.OnScreenInput;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.utils.GraphicsToolkit;

public class ClientScreen extends StageScreen {
	int minTouchSize = 60;
	int insetSize = 10;
	int touchPadSize = 200;

	CommandLinePanel commandLinePanel;
	Touchpad movePad;
	Touchpad shootPad;
	ClientManager client;

	ShapeRenderer shape;

	TextButton showConsole;
	boolean alive;

	EntityRender render;
	ClientInputProducer clientInputProducer;

	public ClientScreen(MainGame mainGame, InputMultiplexer inputMultiplexer) {
		super(mainGame, inputMultiplexer);
		client = new ClientManager();
		clientInputProducer = new OnScreenInput();
		setClearColor(Color.BLACK);

		getClient().getCommand().pushHistory("connect 192.168.43.100; login user"+MathUtils.random(100)+";lobby;");
//		getClient().getCommand().pushHistory("login user2;");
//		getClient().getCommand().pushHistory("lobby");
//
//		getClient().getCommand().pushHistory("connect emptypocketgames.noip.me");
//		
//		getClient().getCommand().pushHistory("connect; login client"+MathUtils.random(100)+"; lobby");
//		getClient().getCommand().pushHistory("host start;connect; login client"+MathUtils.random(100)+"; lobby");

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
		shape = new ShapeRenderer();
		render = new EntityRender();
	}

	@Override
	public void hide() {
		super.hide();
		if (shape != null) {
			shape.dispose();
		}
		shape = null;

		if (commandLinePanel != null) {
			commandLinePanel.dispose();
		}
		commandLinePanel = null;

		if (client != null) {
			client.dispose();
		}
		client = null;
		control = null;
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
			if(client.getEngine() != null){
				Entity ent = client.getEngine().getEntityManager().getEntityById(myEntityId);
				if(ent != null){
					getScreenCamera().position.x = ent.getPos().x;
					getScreenCamera().position.y = ent.getPos().y;
					getScreenCamera().viewportWidth = Gdx.graphics.getWidth();
					getScreenCamera().viewportHeight = Gdx.graphics.getHeight();
					getScreenCamera().update();
				}
			}
		}
		shape.setProjectionMatrix(getScreenCamera().combined);
	}

	@Override
	public void drawScreen(float delta) {
		GraphicsToolkit.draw2DAxis(shape, getScreenCamera(), 100, Color.WHITE);
		if (client.getEngine() != null)
			synchronized (client.getEngine()) {
				render.render(getScreenCamera(), client.getEngine());
			}
	}

	@Override
	public void drawOverlay(float delta) {
	}

	@Override
	public void updateLogic(float delta) {
		super.updateLogic(delta);

		if (client.getEngine() != null) {
			synchronized (client.getEngine()) {
				client.getEngine().update();
			}
		}

		if (client.isLoggedIn()) {
			clientInputProducer.update();
			client.sendInput(clientInputProducer.getInput());
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
