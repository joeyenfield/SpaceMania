package com.emptypockets.spacemania.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.engine.GameEngineClient;
import com.emptypockets.spacemania.engine.GameEngineHost;
import com.emptypockets.spacemania.engine.input.KeyboardPlayerInputProducer;
import com.emptypockets.spacemania.engine.network.client.ClientPlayerAdapter;
import com.emptypockets.spacemania.engine.network.host.HostPlayerAdapter;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntityDestructionListener;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.gui.GameEngineEntitiesRender;
import com.emptypockets.spacemania.gui.tools.StageScreen;
import com.emptypockets.spacemania.gui.tools.TextRender;
import com.emptypockets.spacemania.utils.CameraHelper;
import com.emptypockets.spacemania.utils.OrthoCamController;

public class GameEngineScreen extends StageScreen implements EntityDestructionListener {

	int width = 45000;
	int height = 45000;

	int regionSizeX = 4000;
	int regionSizeY = 4000;

	int viewOffsetX = width + 10;
	int viewOffsetY = height + 10;
	int desiredEntityCount = 2000;
	public static float minVel = 100;
	public static float maxVel = 500;
	public static float bulletVel = 1200;
	public static long bulletShootTimeMin = 400;
	public static long bulletShootTimeMax = 400;

	int clientCount = 36;
	int rowCount = 6;

	GameEngineHost serverGameEngine;
	GameEngineClient clientGameEngines[] = new GameEngineClient[clientCount];

	GameEngineEntitiesRender render;

	CameraHelper cameraHelper = new CameraHelper();
	Rectangle screenViewport = new Rectangle();

	SpriteBatch spriteBatch;
	ShapeRenderer shapeRender;
	TextRender textHelper;

	OrthoCamController controller;

	Vector2 tempPos = new Vector2();

	public GameEngineScreen(MainGame mainGame, InputMultiplexer inputMultiplexer) {
		super(mainGame, inputMultiplexer);
	}

	public GameEntity createShip() {
		GameEntity entity = serverGameEngine.createEntity(GameEntityType.SHIP);

		LinearMovementComponent comp = (LinearMovementComponent) entity.getComponent(ComponentType.LINEAR_MOVEMENT);
		// float progress = (0.1f + 0.8f * (i / (ents - 1f)));
		// entity.linearTransform.data.pos.x = width * progress;
		// entity.linearTransform.data.pos.y = height * progress;
		// comp.data.vel.x = 10;?
		comp.data.vel.x = MathUtils.random(minVel, maxVel) * MathUtils.randomSign();
		comp.data.vel.y = MathUtils.random(minVel, maxVel) * MathUtils.randomSign();
		entity.linearTransform.data.pos.x = MathUtils.random(serverGameEngine.universeRegion.x, serverGameEngine.universeRegion.x + serverGameEngine.universeRegion.width);
		entity.linearTransform.data.pos.y = MathUtils.random(serverGameEngine.universeRegion.y, serverGameEngine.universeRegion.y + serverGameEngine.universeRegion.height);

		return entity;
	}

	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		controller = new OrthoCamController(getScreenCamera());
		controller.setLimitZoom(false);
		// TODO Auto-generated method stub
		super.addInputMultiplexer(input);
		input.addProcessor(controller);
	}

	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(controller);
	}

	@Override
	public void show() {
		super.show();
		setDrawEvents(false);

		setClearColor(Color.BLACK);

		render = new GameEngineEntitiesRender();
		spriteBatch = new SpriteBatch();
		shapeRender = new ShapeRenderer();
		textHelper = new TextRender();

		serverGameEngine = new GameEngineHost();
		serverGameEngine.setUniverseSize(-width / 2, -height / 2, width, height);

		// createShip();

		// Client Connection
		serverGameEngine.hostNetworkProcess.connections = new ArrayList<HostPlayerAdapter>();

		for (int i = 0; i < clientGameEngines.length; i++) {
			GameEngineClient clientGameEngine = new GameEngineClient();
			String base = "";
			for (int j = 0; j < i; j++) {
				base += "\t";
			}
			clientGameEngine.setName(base + "CLIENT[" + i + "]");
			clientGameEngines[i] = clientGameEngine;
			clientGameEngine.setUniverseSize(-width / 2, -height / 2, width, height);
			clientGameEngine.clientNetworkProcess.adapters = new ArrayList<ClientPlayerAdapter>();

			// Setup Client Side
			ClientPlayerAdapter clientAdapter = new ClientPlayerAdapter();
			clientAdapter.setInputProducer(new KeyboardPlayerInputProducer());
			// ent.linearTransform.data.pos.set(0, 0);
			// Setup Host Side
			HostPlayerAdapter hostAdapter = new HostPlayerAdapter();
			hostAdapter.clientId = i;
			hostAdapter.region = new Rectangle(0, 0, regionSizeX, regionSizeY);

			// Link Adapters
			hostAdapter.adapter = clientAdapter;
			clientAdapter.adapter = hostAdapter;

			serverGameEngine.hostNetworkProcess.connections.add(hostAdapter);
			clientGameEngine.clientNetworkProcess.adapters.add(clientAdapter);
		}

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void createStage(Stage stage) {

	}

	@Override
	public void updateLogic(float delta) {
		super.updateLogic(delta);
		serverGameEngine.update(delta);
		for (int i = 0; i < clientGameEngines.length; i++) {
			clientGameEngines[i].update(delta);
		}
		for (int i = 0; i < 10; i++) {
			if (serverGameEngine.entitySystem.getEntityCount() < desiredEntityCount) {
				createShip();
			}
		}
	}

	@Override
	public void drawScreen(float delta) {
		spriteBatch.setProjectionMatrix(getScreenCamera().combined);
		shapeRender.setProjectionMatrix(getScreenCamera().combined);
		cameraHelper.getBounds(getScreenCamera(), screenViewport);

		float pixSize = cameraHelper.getScreenToCameraPixelX(getScreenCamera(), 1);
		tempPos.x = 0;
		tempPos.y = 0;
		render.showDebug = false;
		render.render(serverGameEngine, screenViewport, shapeRender, spriteBatch, textHelper, pixSize, tempPos);

		for (int i = 0; i < clientGameEngines.length; i++) {
			tempPos.x += viewOffsetX;
			if (i % rowCount == 0) {
				tempPos.x = 0;
				tempPos.y += viewOffsetY;
			}
			// tempPos.x = width+100;
			render.showDebug = false;
			render.render(clientGameEngines[i], screenViewport, shapeRender, spriteBatch, textHelper, pixSize, tempPos);
		}
		renderConnections();
		renderTextOverlay();

	}

	private void renderConnections() {
		shapeRender.begin(ShapeType.Line);
		shapeRender.setColor(Color.BLUE);
		if (serverGameEngine.hostNetworkProcess.connections != null) {
			synchronized (serverGameEngine.hostNetworkProcess.connections) {
				int size = serverGameEngine.hostNetworkProcess.connections.size();
				for (int i = 0; i < size; i++) {
					HostPlayerAdapter connection = serverGameEngine.hostNetworkProcess.connections.get(i);
					shapeRender.rect(connection.region.x, connection.region.y, connection.region.width, connection.region.height);
				}
			}
		}
		shapeRender.end();
	}

	private void renderTextOverlay() {
		shapeRender.begin(ShapeType.Filled);
		shapeRender.setColor(Color.WHITE);

		tempPos.set(screenViewport.x + screenViewport.width / 2, screenViewport.y + cameraHelper.getScreenToCameraPixelX(screenCamera, 40));
		float textHeight = cameraHelper.getScreenToCameraPixelX(screenCamera, 50);
		textHelper.render(shapeRender, Integer.toString(Gdx.graphics.getFramesPerSecond()), tempPos, textHeight, screenViewport);
		tempPos.y += textHeight * 1.2f;
		textHelper.render(shapeRender, Integer.toString(serverGameEngine.entitySystem.getEntityCount()), tempPos, textHeight, screenViewport);

		tempPos.y += textHeight * 1.2f;
		textHelper.render(shapeRender, Integer.toString((int) HostPlayerAdapter.dataRate), tempPos, textHeight, screenViewport);

		shapeRender.end();
	}

	@Override
	public void drawOverlay(float delta) {

	}

	@Override
	public void setupAssetManager(AssetManager assetManager) {

	}

	@Override
	public void clearAssetManager(AssetManager assetManager) {

	}

	@Override
	public void entityDestruction(GameEntity entity) {

	}

}