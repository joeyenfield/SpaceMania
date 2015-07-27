package com.emptypockets.spacemania.network.client.payloads.engine;

import com.emptypockets.spacemania.metrics.plotter.DataLogger;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.engine.sync.EntityManagerSync;
import com.emptypockets.spacemania.network.engine.sync.PlayerManagerSync;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ClientEngineEntityManagerSyncPayload extends ClientPayload {
	EntityManagerSync entitySyncData;

	public EntityManagerSync getSyncData() {
		return entitySyncData;
	}

	public void setEntitySyncData(EntityManagerSync syncData) {
		this.entitySyncData = syncData;
	}

	@Override
	public void executePayload(ClientManager clientManager) {
		entitySyncData.writeToEngine(clientManager.getEngine(), false);
		PoolsManager.free(entitySyncData);
		entitySyncData = null;
	}

	public void reset() {
		entitySyncData = null;
	}
}
