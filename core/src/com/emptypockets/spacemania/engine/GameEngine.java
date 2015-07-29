package com.emptypockets.spacemania.engine;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.engine.dynamics.MovementProcessor;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.spatialpartition.CellsGameEntitySpatitionPartition;
import com.emptypockets.spacemania.engine.spatialpartition.PartitionProcessor;
import com.emptypockets.spacemania.utils.CameraHelper;
import com.emptypockets.spacemania.utils.event.EventRecorder;

public class GameEngine {
	int entityCount = 0;
	public Rectangle universeRegion = new Rectangle();
	public EntitySystem entitySystem;
	public CellsGameEntitySpatitionPartition spatialPartition;

	MovementProcessor movementProcessor = new MovementProcessor();
	PartitionProcessor partitionProcessor = new PartitionProcessor();

	CameraHelper cameraHelper = new CameraHelper();
	EventRecorder eventLogger;

	public GameEngine(EventRecorder eventLogger) {
		universeRegion.set(-Constants.DEFAULT_ROOM_SIZE, -Constants.DEFAULT_ROOM_SIZE, 2 * Constants.DEFAULT_ROOM_SIZE, 2 * Constants.DEFAULT_ROOM_SIZE);
		entitySystem = new EntitySystem();
		spatialPartition = new CellsGameEntitySpatitionPartition(this, Constants.ENTITY_SYSTEM_PARTITION_X,Constants.ENTITY_SYSTEM_PARTITION_Y);
		this.eventLogger = eventLogger;
	}

	public void update(float deltaTime) {
		eventLogger.begin("LOGIC-MOVEMENT");
		movementProcessor.manage(entitySystem, deltaTime);
		eventLogger.end("LOGIC-MOVEMENT");
		
		eventLogger.begin("LOGIC-Partition");
		partitionProcessor.manage(entitySystem, deltaTime);
		eventLogger.end("LOGIC-Partition");
		
	}

}
