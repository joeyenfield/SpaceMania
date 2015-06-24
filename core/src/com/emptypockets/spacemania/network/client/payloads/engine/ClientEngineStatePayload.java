package com.emptypockets.spacemania.network.client.payloads.engine;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.engine.EngineState;
import com.emptypockets.spacemania.network.transport.ComsType;

public class ClientEngineStatePayload extends ClientPayload {

	EngineState state;

	public ClientEngineStatePayload() {
		setComsType(ComsType.UDP);
	}

	@Override
	public void executePayload(ClientManager clientManager) {
		state.writeTo(clientManager.getEngine());
	}

	public EngineState getState() {
		return state;
	}

	public void setState(EngineState state) {
		this.state = state;
	}

	@Override
	public void reset() {
		super.reset();
		setComsType(ComsType.UDP);
	}
}
