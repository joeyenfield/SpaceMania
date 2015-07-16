package com.emptypockets.spacemania.network.client.payloads.engine;

import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.payloads.rooms.ClientRoomMessagesPayload;
import com.emptypockets.spacemania.network.engine.sync.EntityManagerSync;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.transport.ComsType;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ServerRoomDataSendProcessor implements SingleProcessor<ServerPlayer> {

	long serverTime;

	ClientRoomMessagesPayload roomMessagePayloads;
	ClientEngineRegionStatePayload engineRegionPayload;

	@Override
	public void process(ServerPlayer player) {

		// Entity Sync Data
		ClientEngineEntityManagerSyncPayload payload = PoolsManager.obtain(ClientEngineEntityManagerSyncPayload.class);
		EntityManagerSync entitySyncData = player.getEntityManagerSync();
		entitySyncData.setTime(serverTime);
		payload.setEntitySyncData(entitySyncData);
		player.send(payload, ComsType.TCP);
		PoolsManager.free(payload);

		// Broadcast Player Entity Managers
		if (System.currentTimeMillis() - player.getLastPlayersBroadcast() > Constants.SERVER_TIME_ROOM_BROADCAST_PLAYER_PEROID) {
			player.setLastPlayersBroadcast(System.currentTimeMillis());
			player.getPlayerManagerSync().read(player.getCurrentRoom().getPlayerManager());

			ClientEnginePlayerManagerSyncPayload playerManagerSyncPayload = PoolsManager.obtain(ClientEnginePlayerManagerSyncPayload.class);
			playerManagerSyncPayload.setPlayerSyncData(player.getPlayerManagerSync());
			player.send(playerManagerSyncPayload, ComsType.TCP);
			PoolsManager.free(playerManagerSyncPayload);
		}

		if (roomMessagePayloads != null) {
			player.send(roomMessagePayloads, ComsType.TCP);
		}
		if (engineRegionPayload != null) {
			player.send(engineRegionPayload, ComsType.TCP);
		}

		entitySyncData.clearAfterDataSend();

	}

	public void clearAfterSend() {
		if (roomMessagePayloads != null) {
			PoolsManager.free(roomMessagePayloads);
		}
		roomMessagePayloads = null;

		if (engineRegionPayload != null) {
			PoolsManager.free(engineRegionPayload);
		}
		engineRegionPayload = null;
	}

	public long getServerTime() {
		return serverTime;
	}

	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}

	public void setRegionState(ClientEngineRegionStatePayload engineRegionPayload) {
		this.engineRegionPayload = engineRegionPayload;
	}

	public void setRoomMessages(ClientRoomMessagesPayload roomMessagePayloads) {
		this.roomMessagePayloads = roomMessagePayloads;
	}
}
