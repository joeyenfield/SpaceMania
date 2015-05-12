package com.emptypockets.spacemania.network.server.payloads;

import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.transport.NetworkPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public abstract class ServerPayload extends NetworkPayload {

    protected ServerManager serverManager;
    protected ClientConnection clientConnection;

    public ServerManager getServerManager() {
        return serverManager;
    }

    public void setServerManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    @Override
    public void reset() {
        super.reset();
        serverManager = null;
        clientConnection = null;
    }
}
