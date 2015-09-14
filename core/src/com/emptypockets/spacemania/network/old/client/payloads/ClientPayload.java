package com.emptypockets.spacemania.network.old.client.payloads;

import com.emptypockets.spacemania.network.common.data.NetworkPayload;
import com.emptypockets.spacemania.network.old.client.ClientManager;

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