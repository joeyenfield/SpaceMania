package com.emptypockets.spacemania.network.client.payloads.engine;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.engine.EngineRegionSync;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ClientEngineRegionStatePayload extends ClientPayload {

	EngineRegionSync state;

	public ClientEngineRegionStatePayload() {
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
