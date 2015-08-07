package com.emptypockets.spacemania.network.old.server.payloads.rooms;

import com.emptypockets.spacemania.network.old.server.ClientConnection;
import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.old.server.payloads.ServerPayload;

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
