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
import com.emptypockets.spacemania.engine.managers.ServerManager;
import com.emptypockets.spacemania.engine.managers.WeaponManager;
import com.emptypockets.spacemania.engine.spatialpartition.CellsGameEntitySpatitionPartition;
import com.emptypockets.spacemania.gui.AssetStore;
import com.emptypockets.spacemania.utils.event.EventRecorder;

public class GameEngine {
	public Rectangle universeRegion = new Rectangle();
	public EntitySystem entitySystem;

	public CellsGameEntitySpatitionPartition spatialPartition;

	public MovementManager movementManager = new MovementManager();
	public PartitionManager partitionManager = new PartitionManager();
	public CollissionManager collissionManager = new CollissionManager();
	public DestructionManager destructionManager = new DestructionManager();
	public ServerManager serverNetworkManager = new ServerManager();
	public WeaponManager weaponManager = new WeaponManager();

	public GameEntityFactory entityFactory;
	public AssetStore assetStore;

	EventRecorder eventLogger;

	public GameEngine(EventRecorder eventLogger) {
		universeRegion.set(-Constants.DEFAULT_ROOM_SIZE, -Constants.DEFAULT_ROOM_SIZE, 2 * Constants.DEFAULT_ROOM_SIZE, 2 * Constants.DEFAULT_ROOM_SIZE);
		entitySystem = new EntitySystem();
		spatialPartition = new CellsGameEntitySpatitionPartition(this, Constants.ENTITY_SYSTEM_PARTITION_X, Constants.ENTITY_SYSTEM_PARTITION_Y);
		this.eventLogger = eventLogger;
		assetStore = new AssetStore();
		entityFactory = new GameEntityFactory(this, assetStore);
	}

	public void update(float deltaTime) {
		if (serverNetworkManager != null) {
			serverNetworkManager.processIncommingData(this);
		}

		movementManager.manage(entitySystem, deltaTime);
		weaponManager.manage(entitySystem, deltaTime);
		partitionManager.manage(entitySystem, deltaTime);
		collissionManager.manage(entitySystem, deltaTime);
		destructionManager.manage(entitySystem, deltaTime);

		if (serverNetworkManager != null) {
			serverNetworkManager.processOutgoingData(this);
		}
	}

	public GameEntity getEntityAtPos(Vector2 tempPos) {
		return spatialPartition.getFirstEntityAtPos(tempPos);
	}

	public float getTime() {
		return System.currentTimeMillis();
	}

	public void addEntity(GameEntity entity) {
		entitySystem.add(entity);
		entity.addListener(spatialPartition);
	}

}
