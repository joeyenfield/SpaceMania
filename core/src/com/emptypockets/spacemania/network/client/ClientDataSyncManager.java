package com.emptypockets.spacemania.network.client;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.GameEngineClient;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.network.common.data.engine.GameEngineState;
import com.emptypockets.spacemania.network.common.data.engine.entity.GameEntityAdded;
import com.emptypockets.spacemania.network.common.data.engine.entity.GameEntityNetworkSync;
import com.emptypockets.spacemania.network.common.data.engine.entity.GameEntityRemoved;

public class ClientDataSyncManager {

	public void apply(GameEngineClient clientEngine, GameEngineState state) {
		int size = 0;
		if (state == null) {
			return;
		}

		if (state.entitySystemState != null) {
			
			// Add entities
			ArrayList<GameEntityAdded> added = state.entitySystemState.addedEntities;
			if (added != null) {
				size = added.size();
				System.out.println("SIZE : "+size);
				for (int i = 0; i < size; i++) {
					GameEntityAdded add = added.get(i);
					clientEngine.createEntity(add.type, add.id);
				}
			}

			// Remove Entities
			ArrayList<GameEntityRemoved> removed = state.entitySystemState.removedEntities;
			if (removed != null) {
				size = removed.size();
				for (int i = 0; i < size; i++) {
					GameEntityRemoved remove = removed.get(i);
					clientEngine.removeEntity(remove.id);
				}
			}
			
			// Update Entities
			ArrayList<GameEntityNetworkSync> statesSync = state.entitySystemState.entityStates;
			if (removed != null) {
				size = statesSync.size();
				for (int i = 0; i < size; i++) {
					GameEntityNetworkSync stateSync = statesSync.get(i);
					GameEntity gameEntity = clientEngine.getEntityById(stateSync.entityId);
					
				}
			}
			
		}
	}
}
