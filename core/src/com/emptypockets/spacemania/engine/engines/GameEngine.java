package com.emptypockets.spacemania.engine.engines;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.engine.GameEntityFactory;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.managers.CollissionManager;
import com.emptypockets.spacemania.engine.managers.DestructionManager;
import com.emptypockets.spacemania.engine.managers.MovementManager;
import com.emptypockets.spacemania.engine.managers.PartitionManager;
import com.emptypockets.spacemania.engine.managers.ServerManager;
import com.emptypockets.spacemania.engine.managers.WeaponManager;
import com.emptypockets.spacemania.engine.spatialpartition.CellsGameEntitySpatitionPartition;
import com.emptypockets.spacemania.gui.AssetStore;
import com.emptypockets.spacemania.network.ClientEngineSyncManager;
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

	public void setUniverseSize(float x, float y, float width, float height) {
		universeRegion.x = x;
		universeRegion.y = y;
		universeRegion.width = width;
		universeRegion.height = height;
	}

	public GameEntity create(float minVel, float maxVel) {
		GameEntity entity = entityFactory.createShipEntity();

		LinearMovementComponent comp = (LinearMovementComponent) entity.getComponent(ComponentType.LINEAR_MOVEMENT);
		// float progress = (0.1f + 0.8f * (i / (ents - 1f)));
		// entity.linearTransform.data.pos.x = width * progress;
		// entity.linearTransform.data.pos.y = height * progress;
		// comp.data.vel.x = 10;?
		comp.data.vel.x = MathUtils.random(minVel, maxVel) * MathUtils.randomSign();
		comp.data.vel.y = MathUtils.random(minVel, maxVel) * MathUtils.randomSign();
		entity.linearTransform.data.pos.x = MathUtils.random(universeRegion.x, universeRegion.x + universeRegion.width);
		entity.linearTransform.data.pos.y = MathUtils.random(universeRegion.y, universeRegion.y + universeRegion.height);

		addEntity(entity);
		return entity;
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
