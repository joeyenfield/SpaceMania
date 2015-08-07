package com.emptypockets.spacemania.network.old.server.payloads;

import com.emptypockets.spacemania.network.old.server.ClientConnection;
import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.transport.data.NetworkPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public abstract class ServerPayload extends NetworkPayload {

    public abstract void executePayload(ClientConnection clientConnection, ServerManager serverManager);
    @Override
    public void reset() {
        super.reset();
    }
}
