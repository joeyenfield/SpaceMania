package com.emptypockets.spacemania.engine;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.managers.CollissionManager;
import com.emptypockets.spacemania.engine.managers.DestructionManager;
import com.emptypockets.spacemania.engine.managers.MovementManager;
import com.emptypockets.spacemania.engine.managers.PartitionManager;
import com.emptypockets.spacemania.engine.spatialpartition.CellsGameEntitySpatitionPartition;
import com.emptypockets.spacemania.utils.CameraHelper;
import com.emptypockets.spacemania.utils.event.EventRecorder;

public class GameEngine {
	int entityCount = 0;
	public Rectangle universeRegion = new Rectangle();
	public EntitySystem entitySystem;
	public CellsGameEntitySpatitionPartition spatialPartition;

	MovementManager movementProcessor = new MovementManager();
	PartitionManager partitionProcessor = new PartitionManager();
	DestructionManager destructionProcessor = new DestructionManager();
	CollissionManager collissionProcessor = new CollissionManager();
	
	CameraHelper cameraHelper = new CameraHelper();
	EventRecorder eventLogger;


	public GameEngine(EventRecorder eventLogger) {
		universeRegion.set(-Constants.DEFAULT_ROOM_SIZE, -Constants.DEFAULT_ROOM_SIZE, 2 * Constants.DEFAULT_ROOM_SIZE, 2 * Constants.DEFAULT_ROOM_SIZE);
		entitySystem = new EntitySystem();
		spatialPartition = new CellsGameEntitySpatitionPartition(this, Constants.ENTITY_SYSTEM_PARTITION_X, Constants.ENTITY_SYSTEM_PARTITION_Y);
		this.eventLogger = eventLogger;
	}

	public void update(float deltaTime) {
		eventLogger.begin("LOGIC-MOVEMENT");
		movementProcessor.manage(entitySystem, deltaTime);
		eventLogger.end("LOGIC-MOVEMENT");

		eventLogger.begin("LOGIC-Partition");
		partitionProcessor.manage(entitySystem, deltaTime);
		eventLogger.end("LOGIC-Partition");

		eventLogger.begin("LOGIC-Collission");
		collissionProcessor.manage(entitySystem, deltaTime);
		eventLogger.end("LOGIC-Collission");
		
		eventLogger.begin("LOGIC-Destruction");
		destructionProcessor.manage(entitySystem, deltaTime);
		destructionProcessor.removeEntities(spatialPartition, entitySystem);
		eventLogger.end("LOGIC-Destruction");
	}

	public GameEntity getEntityAtPos(Vector2 tempPos) {
		return spatialPartition.getFirstEntityAtPos(tempPos);
	}

	
}
