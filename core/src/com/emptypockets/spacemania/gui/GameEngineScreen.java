package com.emptypockets.spacemania.gui;

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
import com.emptypockets.spacemania.engine.engines.GameEngine;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.controls.ControlComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.gui.tools.StageScreen;
import com.emptypockets.spacemania.gui.tools.TextRender;
import com.emptypockets.spacemania.network.ClientEngineSyncManager;
import com.emptypockets.spacemania.utils.CameraHelper;
import com.emptypockets.spacemania.utils.OrthoCamController;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEngineScreen extends StageScreen {

	int width = 10000;
	int height = 10000;
	int desiredEntityCount = 1000;
	public static float minVel = 100;
	public static float maxVel = 200;

	GameEngine serverGameEngine;
	GameEngineRender render;

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

		render = new GameEngineRender();
		spriteBatch = new SpriteBatch();
		shapeRender = new ShapeRenderer();
		textHelper = new TextRender();

		serverGameEngine = new GameEngine(eventLogger);
		serverGameEngine.setupEngine(width, height);
		
		serverGameEngine.serverNetworkManager.connections = new ArrayList<ClientEngineSyncManager>();
		for (int i = 0; i < 0; i++) {
			ClientEngineSyncManager con = new ClientEngineSyncManager();

			con.clientId = i;
			con.entityId = serverGameEngine.create(minVel, maxVel).entityId;
			con.region = new Rectangle(0, 0, 2048, 2048);
			serverGameEngine.serverNetworkManager.connections.add(con);
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

		for (int i = 0; i < 10; i++) {
			if (serverGameEngine.entitySystem.getEntityCount() < desiredEntityCount) {
				serverGameEngine.create(minVel, maxVel);
			}
		}
	}
	
	

	@Override
	public void drawScreen(float delta) {
		spriteBatch.setProjectionMatrix(getScreenCamera().combined);
		shapeRender.setProjectionMatrix(getScreenCamera().combined);
		cameraHelper.getBounds(getScreenCamera(), screenViewport);

		render.render(spriteBatch, serverGameEngine, screenViewport);
		render.renderDebug(shapeRender, serverGameEngine, textHelper, screenViewport);
		
		renderConnections();
		renderTextOverlay();

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
		textHelper.render(shapeRender, Integer.toString((int) ClientEngineSyncManager.dataRate), tempPos, textHeight, screenViewport);

		shapeRender.end();
	}

	private void renderConnections() {
		shapeRender.begin(ShapeType.Line);
		shapeRender.setColor(Color.BLUE);
		if (serverGameEngine.serverNetworkManager.connections != null) {
			synchronized (serverGameEngine.serverNetworkManager.connections) {
				int size = serverGameEngine.serverNetworkManager.connections.size();
				for (int i = 0; i < size; i++) {
					ClientEngineSyncManager connection = serverGameEngine.serverNetworkManager.connections.get(i);
					shapeRender.rect(connection.region.x, connection.region.y, connection.region.width, connection.region.height);
				}
			}
		}
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

}