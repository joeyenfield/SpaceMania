package com.emptypockets.spacemania.network.old.server.payloads.rooms;

import com.emptypockets.spacemania.network.old.server.ClientConnection;
import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.old.server.payloads.ServerPayload;

/**
 * Created by jenfield on 15/05/2015.
 */
public class SpawnPlayerRequestPayload extends ServerPayload {
    @Override
    public void executePayload(ClientConnection clientConnection, ServerManager serverManager) {
        serverManager.spawnPlayer(clientConnection);
    }
}
