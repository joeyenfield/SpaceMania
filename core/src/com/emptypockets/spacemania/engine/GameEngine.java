package com.emptypockets.spacemania.engine;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.engine.dynamics.MovementProcessor;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.spatialpartition.CellsGameEntitySpatitionPartition;
import com.emptypockets.spacemania.utils.CameraHelper;

public class GameEngine {
	int entityCount = 0;
	public Rectangle universeRegion = new Rectangle();
	public EntitySystem entitySystem;
	public CellsGameEntitySpatitionPartition spatialPartition;

	MovementProcessor movementProcessor = new MovementProcessor();
	CameraHelper cameraHelper = new CameraHelper();

	public GameEngine() {
		universeRegion.set(-Constants.DEFAULT_ROOM_SIZE, -Constants.DEFAULT_ROOM_SIZE, 2 * Constants.DEFAULT_ROOM_SIZE, 2 * Constants.DEFAULT_ROOM_SIZE);
		entitySystem = new EntitySystem();
		spatialPartition = new CellsGameEntitySpatitionPartition(universeRegion, Constants.ENTITY_SYSTEM_PARTITION_X, Constants.ENTITY_SYSTEM_PARTITION_Y);
	}


	public void update(float deltaTime) {
		movementProcessor.manage(entitySystem, deltaTime);
		spatialPartition.manage(entitySystem, deltaTime);
	}

}
