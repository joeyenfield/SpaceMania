package com.emptypockets.spacemania.network.client.payloads.engine;

import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.engine.sync.EntityManagerSync;

public class ClientEngineEntityManagerSyncPayload extends ClientPayload {
	EntityManagerSync syncData;

	public EntityManagerSync getSyncData() {
		return syncData;
	}

	public void setSyncData(EntityManagerSync syncData) {
		this.syncData = syncData;
	}

	@Override
	public void executePayload(ClientManager clientManager) {
		syncData.writeToEngine(clientManager.getEngine());
		Pools.free(syncData);
	}

}
