package com.emptypockets.spacemania.network;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.engine.engines.GameEngine;
import com.emptypockets.spacemania.engine.entitysystem.EntityDestructionListener;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.network.NetworkServerComponent;
import com.emptypockets.spacemania.network.transport.data.engine.GameEngineState;
import com.emptypockets.spacemania.network.transport.data.engine.entity.ComponentDataStore;
import com.emptypockets.spacemania.network.transport.data.engine.entity.EntitySystemState;
import com.emptypockets.spacemania.network.transport.data.engine.entity.GameEntityAdded;
import com.emptypockets.spacemania.network.transport.data.engine.entity.GameEntityNetworkSync;
import com.emptypockets.spacemania.network.transport.data.engine.entity.GameEntityRemoved;
import com.emptypockets.spacemania.utils.PoolsManager;

public class EngineSyncManager implements EntityDestructionListener {

	/**
	 * Previous DAta
	 */

	ArrayList<GameEntity> tempEntities = new ArrayList<GameEntity>();
	ArrayList<GameEntity> tempCurrentEntities = new ArrayList<GameEntity>();

	ArrayList<GameEntity> lastEntities = new ArrayList<GameEntity>();
	HashMap<GameEntity, ComponentDataStore> lastData = new HashMap<GameEntity, ComponentDataStore>();

	GameEngineState currentState;

	public EngineSyncManager() {

	}

	private synchronized void updateEntityState(GameEngine engine,Rectangle region) {
		engine.spatialPartition.searchAnyMask(region, ComponentType.NETWORK_SERVER.getMask(), tempEntities);

		int size = 0;

		tempCurrentEntities.addAll(lastEntities);
		size = tempEntities.size();
		// Work out whats been added;
		for (int i = 0; i < size; i++) {
			GameEntity ent = tempEntities.get(i);
			if (!tempCurrentEntities.contains(ent)) {
				addEntity(ent);
			}
		}

		// Work out entities been removed
		size = tempCurrentEntities.size();
		for (int i = 0; i < size; i++) {
			GameEntity ent = tempCurrentEntities.get(i);
			if (!tempEntities.contains(ent)) {
				removeEntity(ent);
			}
		}

		// Clear Temp Holders
		tempEntities.clear();
		tempCurrentEntities.clear();

		// Go through entities and fetch any updates
		for (GameEntity ent : lastEntities) {
			GameEntityNetworkSync syncMessage = PoolsManager.obtain(GameEntityNetworkSync.class);
			if (ent.getComponent(ComponentType.NETWORK_SERVER, NetworkServerComponent.class).shouldSendData(lastData.get(ent), syncMessage)) {
				getCurrentState().entitySystemState.entityStates.add(syncMessage);
			} else {
				// Free Message
				PoolsManager.free(syncMessage);
			}
		}
	}

	private synchronized GameEngineState getCurrentState() {
		if (currentState == null) {
			currentState = Pools.obtain(GameEngineState.class);
			currentState.entitySystemState = Pools.obtain(EntitySystemState.class);
		}
		return currentState;
	}

	public synchronized GameEngineState getState(GameEngine engine, Rectangle currentRegion) {
		updateEntityState(engine,currentRegion);

		// Flip Out State with a new state
		GameEngineState state = currentState;
		currentState = null;
		getCurrentState(); // Should create a new state object
		// Return Old State
		state.serverTime = engine.getTime();
		return state;
	}

	public synchronized void addEntity(GameEntity entity) {
		GameEntityAdded entAdd = PoolsManager.obtain(GameEntityAdded.class);
		entAdd.id = entity.entityId;
		entAdd.type = entity.type;
		getCurrentState().entitySystemState.addedEntities.add(entAdd);

		lastData.put(entity, PoolsManager.obtain(ComponentDataStore.class));
		lastEntities.add(entity);
		entity.addListener(this);
	}

	public synchronized void removeEntity(GameEntity entity, boolean detatchListener) {
		GameEntityRemoved removed = PoolsManager.obtain(GameEntityRemoved.class);
		removed.id = entity.entityId;
		getCurrentState().entitySystemState.removedEntities.add(removed);

		PoolsManager.free(lastData.remove(entity));
		lastEntities.remove(entity);
		if (detatchListener) {
			entity.removeListener(this);
		}
	}

	public synchronized void removeEntity(GameEntity entity) {
		removeEntity(entity, true);
	}

	@Override
	public synchronized void entityDestruction(GameEntity entity) {
		// NO need to remove listener when destruction as its going to be removed by the destruction
		removeEntity(entity, false);
	}
}
