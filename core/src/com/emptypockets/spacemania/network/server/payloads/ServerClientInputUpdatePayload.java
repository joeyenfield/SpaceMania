package com.emptypockets.spacemania.network.server.payloads;

import com.emptypockets.spacemania.network.client.input.ClientInput;
import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;

public class ServerClientInputUpdatePayload extends ServerPayload {
	ClientInput input;
	
	public ServerClientInputUpdatePayload() {
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
