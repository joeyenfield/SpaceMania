package com.emptypockets.spacemania.network.client.payloads;

import com.badlogic.gdx.utils.Pool;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.transport.NetworkPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public abstract class ClientPayload extends NetworkPayload implements Pool.Poolable {

    protected ClientManager clientManager;

    public ClientManager getClientManager() {
        return clientManager;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void reset() {
        super.reset();
        clientManager = null;
    }
}
