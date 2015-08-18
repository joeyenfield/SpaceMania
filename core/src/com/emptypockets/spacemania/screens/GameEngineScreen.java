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
import com.emptypockets.spacemania.engine.systems.entitysystem.EntityDestructionListener;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.controls.ControlComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.gui.GameEngineEntitiesRender;
import com.emptypockets.spacemania.gui.tools.StageScreen;
import com.emptypockets.spacemania.gui.tools.TextRender;
import com.emptypockets.spacemania.network.client.ClientPlayerAdapter;
import com.emptypockets.spacemania.network.host.HostPlayerAdapter;
import com.emptypockets.spacemania.utils.CameraHelper;
import com.emptypockets.spacemania.utils.OrthoCamController;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEngineScreen extends StageScreen implements EntityDestructionListener {

	int width = 300;
	int height = 300;
	int desiredEntityCount = 10;
	public static float minVel = 1;
	public static float maxVel = 10;

	GameEngineHost serverGameEngine;
	GameEngineClient clientGameEngine;

	GameEngineEntitiesRender render;

	CameraHelper cameraHelper = new CameraHelper();
	Rectangle screenViewport = new Rectangle();

	SpriteBatch spriteBatch;
	ShapeRenderer shapeRender;
	OrthoCamController controller;

	TextRender textHelper;
	Vector2 tempPos = new Vector2();

	GameEntity selectedEntity = null;

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

		clientGameEngine = new GameEngineClient();
		clientGameEngine.setUniverseSize(-width / 2, -height / 2, width, height);

		createShip();

		// Client Connection
		clientGameEngine.clientNetworkProcess.adapters = new ArrayList<ClientPlayerAdapter>();
		serverGameEngine.hostNetworkProcess.connections = new ArrayList<HostPlayerAdapter>();

		for (int i = 0; i < 1; i++) {
			// Setup Client Side
			ClientPlayerAdapter clientAdapter = new ClientPlayerAdapter();

			// Setup Host Side
			HostPlayerAdapter hostConnection = new HostPlayerAdapter();
			hostConnection.clientId = i;
			hostConnection.entityId = createShip().entityId;
			hostConnection.region = new Rectangle(0, 0, 2048, 2048);
			hostConnection.adapter = clientAdapter;

			serverGameEngine.hostNetworkProcess.connections.add(hostConnection);
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
		clientGameEngine.update(delta);
	}

	@Override
	public void drawScreen(float delta) {
		spriteBatch.setProjectionMatrix(getScreenCamera().combined);
		shapeRender.setProjectionMatrix(getScreenCamera().combined);
		cameraHelper.getBounds(getScreenCamera(), screenViewport);

		float pixSize = cameraHelper.getScreenToCameraPixelX(getScreenCamera(), 1);
		tempPos.x = 0;
		tempPos.y = 0;
		render.render(serverGameEngine, screenViewport, shapeRender, spriteBatch, textHelper, pixSize, tempPos);

		tempPos.x = width + 20;
		// tempPos.y = height + 20;
		render.render(clientGameEngine, screenViewport, shapeRender, spriteBatch, textHelper, pixSize, tempPos);
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
	public boolean tap(float x, float y, int count, int button) {
		if (count >= 1) {
			tempPos.x = x;
			tempPos.y = y;
			cameraHelper.screenToWorld(getScreenCamera(), tempPos);

			if (this.selectedEntity != null) {
				this.selectedEntity.removeComponent(ComponentType.CONTROL);
				this.selectedEntity.removeListener(this);
				LinearMovementComponent comp = (LinearMovementComponent) this.selectedEntity.getComponent(ComponentType.LINEAR_MOVEMENT);
				// float progress = (0.1f + 0.8f * (i / (ents - 1f)));
				// entity.linearTransform.data.pos.x = width * progress;
				// entity.linearTransform.data.pos.y = height * progress;
				// comp.data.vel.x = 10;?
				comp.data.vel.x = MathUtils.random(minVel, maxVel) * MathUtils.randomSign();
				comp.data.vel.y = MathUtils.random(minVel, maxVel) * MathUtils.randomSign();
			}
			this.selectedEntity = serverGameEngine.getEntityAtPos(tempPos);

			if (this.selectedEntity != null) {
				ControlComponent control = PoolsManager.obtain(ControlComponent.class);
				control.setupData();
				this.selectedEntity.addComponent(control);
				this.selectedEntity.addListener(this);
			}
		}
		return false;
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
		this.selectedEntity = null;

	}

}