package com.emptypockets.spacemania.network.server.payloads;

import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.transport.NetworkPayload;

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
