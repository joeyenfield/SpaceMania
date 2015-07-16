package com.emptypockets.spacemania.network.server.payloads.rooms;

import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;

/**
 * Created by jenfield on 15/05/2015.
 */
public class SpawnPlayerRequestPayload extends ServerPayload {
    @Override
    public void executePayload(ClientConnection clientConnection, ServerManager serverManager) {
        serverManager.spawnPlayer(clientConnection);
    }
}
