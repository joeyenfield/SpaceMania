package com.emptypockets.spacemania.network.old.client.payloads.rooms;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.old.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.old.engine.sync.EntityManagerSync;
import com.emptypockets.spacemania.utils.PoolsManager;

/**
 * Created by jenfield on 15/05/2015.
 */
public class JoinRoomSuccessPayload extends ClientPayload {
	ClientRoom room;
	EntityManagerSync initialSync;

	public void setRoom(ClientRoom room) {
		this.room = room;
	}

	public void setInitialSync(EntityManagerSync initialSync) {
		this.initialSync = initialSync;
	}

	@Override
	public void executePayload(ClientManager clientManager) {
		clientManager.setCurrentRoom(room);
		clientManager.getEngine().getEntityManager().clear();
		initialSync.writeToEngine(clientManager.getEngine(), true);
		PoolsManager.free(initialSync);
		initialSync = null;
	}

	@Override
	public void reset() {
		super.reset();
		room = null;
		initialSync = null;
	}
}
