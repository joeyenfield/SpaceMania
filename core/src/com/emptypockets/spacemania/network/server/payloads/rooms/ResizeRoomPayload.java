package com.emptypockets.spacemania.network.server.payloads.rooms;

import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;

public class ResizeRoomPayload extends ServerPayload{
	float size;

	@Override
	public void executePayload(ClientConnection clientConnection, ServerManager serverManager) {
		serverManager.requestResizeRoom(clientConnection, size);
	}

	public void setRoomSize(int size) {
		this.size = size;
	}
	
	
}
