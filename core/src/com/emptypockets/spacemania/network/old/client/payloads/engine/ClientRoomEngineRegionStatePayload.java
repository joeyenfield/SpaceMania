package com.emptypockets.spacemania.network.old.client.payloads.engine;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.old.engine.EngineRegionSync;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ClientRoomEngineRegionStatePayload extends ClientPayload {

	EngineRegionSync state;

	public ClientRoomEngineRegionStatePayload() {
	}

	@Override
	public void executePayload(ClientManager clientManager) {
		state.writeTo(clientManager.getEngine());
		PoolsManager.free(state);
	}

	public EngineRegionSync getState() {
		return state;
	}

	public void setState(EngineRegionSync state) {
		this.state = state;
	}

	@Override
	public void reset() {
		super.reset();
		this.state = null;
	}
}
