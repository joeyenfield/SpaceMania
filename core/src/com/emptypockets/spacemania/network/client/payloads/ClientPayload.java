package com.emptypockets.spacemania.network.client.payloads;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.transport.NetworkPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public abstract class ClientPayload extends NetworkPayload {

    public abstract void executePayload(ClientManager clientManager);
    @Override
    public void reset() {
        super.reset();
    }
}
