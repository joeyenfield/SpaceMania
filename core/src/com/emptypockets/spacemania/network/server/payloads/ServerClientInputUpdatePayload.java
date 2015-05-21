package com.emptypockets.spacemania.network.server.payloads;

import com.emptypockets.spacemania.network.client.input.ClientInput;
import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.transport.ComsType;

public class ServerClientInputUpdatePayload extends ServerPayload {
	ClientInput input;
	
	public ServerClientInputUpdatePayload() {
		setComsType(ComsType.UDP);
	}
	@Override
	public void executePayload(ClientConnection clientConnection, ServerManager serverManager) {
		clientConnection.getPlayer().getClientInput().read(input);
	}
	public ClientInput getInput() {
		return input;
	}
	public void setInput(ClientInput input) {
		this.input = input;
	}
	
	
	
}
