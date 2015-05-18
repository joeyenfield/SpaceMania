package com.emptypockets.spacemania.network.server.payloads.rooms;

import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;

/**
 * Created by jenfield on 16/05/2015.
 */
public class RequestRoomListPayload extends ServerPayload {
    @Override
    public void executePayload(ClientConnection clientConnection, ServerManager serverManager) {
        serverManager.requestRoomList(clientConnection);
    }
}
