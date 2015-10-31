package com.emptypockets.spacemania.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.GameEngineClient;
import com.emptypockets.spacemania.engine.GameEngineHost;
import com.emptypockets.spacemania.engine.input.DebugOnScreenPlayerInputProducer;
import com.emptypockets.spacemania.engine.input.NamedInputMultiplexer;
import com.emptypockets.spacemania.engine.network.client.ClientPlayerAdapter;
import com.emptypockets.spacemania.engine.network.host.HostPlayerAdapter;
import com.emptypockets.spacemania.engine.systems.controls.GameEngineControler;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntityDestructionListener;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.render.RenderComponent;
import com.emptypockets.spacemania.gui.BackgroundRender;
import com.emptypockets.spacemania.gui.GameEngineEntitiesRender;
import com.emptypockets.spacemania.gui.tools.StageScreen;
import com.emptypockets.spacemania.gui.tools.TextRender;
import com.emptypockets.spacemania.utils.CameraHelper;
import com.emptypockets.spacemania.utils.PanAndZoomCamController;

public class GameEngineScreen extends StageScreen implements EntityDestructionListener {

	public static float minVelEnemy = 100;
	public static float maxVelEnemy = 210;
	public static float enemySearchWindow = 1000;

	public static float velShip = 200;
	public static float bulletVel = 1.5f * velShip;

	public static long bulletShootTimeMin = 200;
	public static long bulletShootTimeMax = 200;

	public static long hostNetowrkPeroid = 100;

	int width = 500;
	int height = 500;

	int regionSizeX = 4000;
	int regionSizeY = 4000;

	int viewOffsetX = width + 10;
	int viewOffsetY = height + 10;

	int desiredEntityCount = 2;
	int clientCount = 1;
	int rowCount = 2;

	public boolean debugDisplay = false;
	public boolean centerOnEntity = false;
	public boolean centerOnServer = false;

	GameEngineHost serverGameEngine;
	GameEngineClient clientGameEngines[] = new GameEngineClient[clientCount];

	BackgroundRender backroundRender;
	GameEngineEntitiesRender entityRender;

	CameraHelper cameraHelper = new CameraHelper();
	Rectangle screenViewport = new Rectangle();

	SpriteBatch spriteBatch;
	ShapeRenderer shapeRender;
	TextRender textHelper;

	PanAndZoomCamController cameraController;
	GameEngineControler gameEngineControler;

	Vector2 tempPos = new Vector2();

	DebugOnScreenPlayerInputProducer inputProducer = null;

	GameEngine selectedEngine = null;

	Color debugGridColor = new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.2f);

	Preferences gameEnginePrefs;

	public GameEngineScreen(MainGame mainGame, NamedInputMultiplexer inputMultiplexer) {
		super(mainGame, inputMultiplexer);
		gameEnginePrefs = Gdx.app.getPreferences("gameEnginePrefs");

		gameEnginePrefs.putInteger("runCount", gameEnginePrefs.getInteger("runCount") + 1);
		gameEnginePrefs.flush();
	}

	@Override
	public void addInputMultiplexer(NamedInputMultiplexer input) {
		// TODO Auto-generated method stub
		super.addInputMultiplexer(input);

		gameEngineControler = new GameEngineControler(getScreenCamera());
		input.addProcessor(gameEngineControler, "ENT-CONT");

		cameraController = new PanAndZoomCamController(getScreenCamera());
		cameraController.setLimitZoom(false);
		input.addProcessor(cameraController, "CAM-CONT");
	}

	@Override
	public void removeInputMultiplexer(NamedInputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(cameraController);
		input.removeProcessor(gameEngineControler);
	}

	@Override
	public void show() {
		super.show();
		setDrawEvents(false);

		setClearColor(Color.BLACK);

		entityRender = new GameEngineEntitiesRender();
		backroundRender = new BackgroundRender();
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
		inputProducer.layoutGui();
	}

	@Override
	public void createStage(Stage stage) {
		inputProducer = new DebugOnScreenPlayerInputProducer(getSkin(), this);
		stage.addActor(inputProducer);
	}

	@Override
	public void updateLogic(float delta) {
		super.updateLogic(delta);
		serverGameEngine.update(delta);
		for (int i = 0; i < clientGameEngines.length; i++) {
			clientGameEngines[i].update(delta);
		}
		for (int i = 0; i < 10; i++) {
			if (serverGameEngine.entitySystem.getEntityCount(GameEntityType.ENEMY) < desiredEntityCount) {
				serverGameEngine.createEntity(GameEntityType.ENEMY);
			}
		}

		if (centerOnEntity) {
			// If centered on client engine
			if (selectedEngine instanceof GameEngineClient) {
				GameEngineClient selectedClient = (GameEngineClient) selectedEngine;
				if (selectedClient != null && selectedClient.clientNetworkProcess != null && selectedClient.clientNetworkProcess.adapters != null && selectedClient.clientNetworkProcess.adapters.size() > 0) {
					int entityId = selectedClient.clientNetworkProcess.adapters.get(0).adapter.entityId;
					GameEntity ent = serverGameEngine.getEntityById(entityId);
					if (ent != null) {
						tempPos.set(ent.linearTransform.state.pos);
						if (!centerOnServer) {
							tempPos.add(selectedClient.worldRenderOffset);
						}
						getScreenCamera().position.set(tempPos, 0);
					}
				}
			}
		}
	}

	@Override
	public void drawScreen(float delta) {
		spriteBatch.setProjectionMatrix(getScreenCamera().combined);
		shapeRender.setProjectionMatrix(getScreenCamera().combined);
		cameraHelper.getBounds(getScreenCamera(), screenViewport);

		// if(debugDisplay){
		// GraphicsToolkit.draw2DAxis(shapeRender, getScreenCamera(), 500,
		// debugGridColor);
		// }
		float pixSize = cameraHelper.getScreenToCameraPixelX(getScreenCamera(), 1);
		tempPos.x = 0;
		tempPos.y = 0;
		entityRender.showDebug = debugDisplay;
		serverGameEngine.worldRenderOffset.set(tempPos);

		backroundRender.render(serverGameEngine, screenViewport, shapeRender, spriteBatch, textHelper, pixSize);
		entityRender.render(serverGameEngine, screenViewport, shapeRender, spriteBatch, textHelper, pixSize);
		entityRender.renderBounds(serverGameEngine, screenViewport, shapeRender, (selectedEngine == serverGameEngine) ? Color.RED : Color.GREEN);

		for (int i = 0; i < clientGameEngines.length; i++) {
			tempPos.x += viewOffsetX;
			if (i % rowCount == 0) {
				tempPos.x = 0;
				tempPos.y += viewOffsetY;
			}
			// tempPos.x = width+100;
			entityRender.showDebug = debugDisplay;
			clientGameEngines[i].worldRenderOffset.set(tempPos);
			entityRender.render(clientGameEngines[i], screenViewport, shapeRender, spriteBatch, textHelper, pixSize);
			entityRender.renderBounds(clientGameEngines[i], screenViewport, shapeRender, (selectedEngine == clientGameEngines[i]) ? Color.RED : Color.GREEN);
		}
		renderPlayerConnections();
		renderTextOverlay();

		if (selectedEngine != null) {
			gameEngineControler.render(selectedEngine, screenViewport, shapeRender, spriteBatch, textHelper, pixSize);
		}
	}

	private void renderPlayerConnections() {
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

		tempPos.y += textHeight * 1.2f;
		textHelper.render(shapeRender, Integer.toString(gameEnginePrefs.getInteger("runCount")), tempPos, textHeight, screenViewport);

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

	@Override
	public boolean tap(float x, float y, int count, int button) {
		boolean captureControl = false;
		tempPos.x = x;
		tempPos.y = y;
		cameraHelper.screenToWorld(getScreenCamera(), tempPos);

		if (count > 1) {
			clearControls();
			// Check double tap inside server
			if (serverGameEngine.containsWorld(tempPos)) {
				setSelectedEngine(serverGameEngine);
				int closestPlayer = 0;
				float closestDistance = Float.MAX_VALUE;

				float distance = Float.MAX_VALUE;
				// Get Nearest

				for (int i = 0; i < serverGameEngine.hostNetworkProcess.connections.size(); i++) {
					HostPlayerAdapter player = serverGameEngine.hostNetworkProcess.connections.get(i);
					int playerId = player.adapter.adapter.entityId;
					GameEntity ent = serverGameEngine.getEntityById(playerId);
					if (ent != null) {
						distance = ent.getPos().dst(tempPos);
						if (distance < closestDistance) {
							closestDistance = distance;
							closestPlayer = playerId;
						}
					}
				}
				// get coresponding client Id
				if (closestPlayer != 0 && closestDistance < 100) {
					for (int i = 0; i < clientGameEngines.length; i++) {
						GameEngineClient client = clientGameEngines[i];
						for (int j = 0; j < client.clientNetworkProcess.adapters.size(); j++) {
							if (client.clientNetworkProcess.adapters.get(j).adapter.entityId == closestPlayer) {
								setControl(client);
								captureControl = true;
							}
						}
					}
				}
			} else {
				for (int i = 0; i < clientGameEngines.length; i++) {
					if (clientGameEngines[i].containsWorld(tempPos)) {
						setControl(clientGameEngines[i]);
						captureControl = true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keyCode) {
		if (keyCode == Keys.F12) {
			debugDisplay = !debugDisplay;
			return true;
		} else if (keyCode == Keys.F11) {
			centerOnEntity = !centerOnEntity;
			return true;
		} else if (keyCode == Keys.F10) {
			centerOnServer = !centerOnServer;
			return true;
		}
		return super.keyUp(keyCode);
	}

	public void clearControls() {
		for (int i = 0; i < clientGameEngines.length; i++) {
			clientGameEngines[i].clientNetworkProcess.adapters.get(0).setInputProducer(null);
			int id = clientGameEngines[i].clientNetworkProcess.adapters.get(0).adapter.entityId;
			GameEntity clientEnt = clientGameEngines[i].getEntityById(id);
			if (clientEnt != null) {
				clientEnt.getComponent(ComponentType.RENDER, RenderComponent.class).clearColor();
			}
			GameEntity serverEnt = serverGameEngine.getEntityById(id);
			if (serverEnt != null) {
				serverEnt.getComponent(ComponentType.RENDER, RenderComponent.class).clearColor();
			}
		}
		setSelectedEngine(null);
	}

	public void setControl(GameEngineClient client) {
		client.clientNetworkProcess.adapters.get(0).setInputProducer(inputProducer);
		int id = client.clientNetworkProcess.adapters.get(0).adapter.entityId;
		GameEntity clientEnt = client.getEntityById(id);
		if (clientEnt != null) {
			clientEnt.getComponent(ComponentType.RENDER, RenderComponent.class).getColor().set(Color.GREEN);
		}
		GameEntity serverEnt = serverGameEngine.getEntityById(id);
		if (serverEnt != null) {
			serverEnt.getComponent(ComponentType.RENDER, RenderComponent.class).getColor().set(Color.GREEN);
		}

		setSelectedEngine(client);
	}

	public void setSelectedEngine(GameEngine client) {
		selectedEngine = client;
		gameEngineControler.setGameEngine(client);
	}

	public GameEntity getEntity(Vector2 currentTouch) {
		return serverGameEngine.getEntityAtPos(currentTouch);
	}
}