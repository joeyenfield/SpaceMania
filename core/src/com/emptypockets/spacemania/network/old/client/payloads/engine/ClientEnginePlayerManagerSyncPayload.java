package com.emptypockets.spacemania.network.old.client.payloads.engine;

import com.emptypockets.spacemania.metrics.plotter.DataLogger;
import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.old.engine.sync.EntityManagerSync;
import com.emptypockets.spacemania.network.old.engine.sync.PlayerManagerSync;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ClientEnginePlayerManagerSyncPayload extends ClientPayload {
	PlayerManagerSync playerSyncData;

	@Override
	public void executePayload(ClientManager clientManager) {
		playerSyncData.write(clientManager.getEngine().getPlayerData());
		PoolsManager.free(playerSyncData);
		playerSyncData = null;
	}

	public void setPlayerSyncData(PlayerManagerSync playerSyncData) {
		this.playerSyncData = playerSyncData;
	};

	public void reset() {
		playerSyncData = null;
	}
}