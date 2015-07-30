package com.emptypockets.spacemania.engine;

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
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.destruction.DestructionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.gui.renderer.TextRender;
import com.emptypockets.spacemania.gui.tools.StageScreen;
import com.emptypockets.spacemania.utils.CameraHelper;
import com.emptypockets.spacemania.utils.OrthoCamController;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEngineScreen extends StageScreen {

	int width = 10000;
	int height = 10000;
	int desiredEntityCount = 1000;

	GameEngine gameEngine;
	GameEntityFactory entityFactory;
	AssetStore assetStore;
	GameEngineRender render;

	CameraHelper cameraHelper = new CameraHelper();
	Rectangle screenViewport = new Rectangle();

	SpriteBatch spriteBatch;
	ShapeRenderer shapeRender;
	OrthoCamController controller;

	TextRender textHelper;
	Vector2 tempPos = new Vector2();

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
		setClearColor(Color.BLACK);
		assetStore = new AssetStore();
		gameEngine = new GameEngine(eventLogger);
		entityFactory = new GameEntityFactory(gameEngine, assetStore);
		render = new GameEngineRender();
		spriteBatch = new SpriteBatch();
		shapeRender = new ShapeRenderer();
		textHelper = new TextRender();

		gameEngine.universeRegion.x = 0;
		gameEngine.universeRegion.y = 0;
		gameEngine.universeRegion.width = width;
		gameEngine.universeRegion.height = height;

//		for (int i = 0; i < desiredEntityCount; i++) {
//			GameEntity entity = create();
//		}

		setDrawEvents(false);
	}

	public GameEntity create() {
		GameEntity entity = entityFactory.createEntity(gameEngine.entityCount++);
		gameEngine.entitySystem.add(entity);
		LinearMovementComponent comp = (LinearMovementComponent) entity.getComponent(ComponentType.LINEAR_MOVEMENT);
		// float progress = (0.1f + 0.8f * (i / (ents - 1f)));
		// entity.linearTransform.data.pos.x = width * progress;
		// entity.linearTransform.data.pos.y = height * progress;
		// comp.data.vel.x = 10;?
		comp.data.vel.x = MathUtils.random(5, 50) * MathUtils.randomSign();
		comp.data.vel.y = MathUtils.random(5, 50) * MathUtils.randomSign();
		entity.linearTransform.data.pos.x = MathUtils.random(gameEngine.universeRegion.x, gameEngine.universeRegion.x + gameEngine.universeRegion.width);
		entity.linearTransform.data.pos.y = MathUtils.random(gameEngine.universeRegion.y, gameEngine.universeRegion.y + gameEngine.universeRegion.height);

		return entity;
	}

	@Override
	public void initializeRender() {

		super.initializeRender();

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
		gameEngine.update(delta);

		if (gameEngine.entitySystem.getEntityCount() < desiredEntityCount) {
			create();
		}
	}

	@Override
	public void drawScreen(float delta) {
		spriteBatch.setProjectionMatrix(getScreenCamera().combined);
		shapeRender.setProjectionMatrix(getScreenCamera().combined);
		cameraHelper.getBounds(getScreenCamera(), screenViewport);

		shapeRender.begin(ShapeType.Line);
		shapeRender.setColor(Color.RED);
		shapeRender.rect(gameEngine.universeRegion.x, gameEngine.universeRegion.y, gameEngine.universeRegion.width, gameEngine.universeRegion.height);
		shapeRender.end();

		// gameEngine.spatialPartition.renderDebug(shapeRender, textHelper,
		// screenViewport);

		render.batch = spriteBatch;
		spriteBatch.begin();
		render.render(gameEngine, screenViewport);
		spriteBatch.end();

		tempPos.set(screenViewport.x + screenViewport.width / 2, screenViewport.y + cameraHelper.getScreenToCameraPixelX(screenCamera, 40));
		shapeRender.begin(ShapeType.Filled);
		shapeRender.setColor(Color.WHITE);
		textHelper.render(shapeRender, Integer.toString(Gdx.graphics.getFramesPerSecond()), tempPos, cameraHelper.getScreenToCameraPixelX(screenCamera, 50), screenViewport);
		shapeRender.end();

	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		System.out.println("HERE "+count);
		if(count > 2){
			tempPos.x = x;
			tempPos.y = y;
			cameraHelper.screenToWorld(getScreenCamera(), tempPos);
			GameEntity ent = gameEngine.getEntityAtPos(tempPos);
			System.out.println(ent);
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