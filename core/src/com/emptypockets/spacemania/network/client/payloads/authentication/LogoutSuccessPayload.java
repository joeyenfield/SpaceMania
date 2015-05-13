package com.emptypockets.spacemania.network.client.payloads.authentication;

import com.emptypockets.spacemania.network.client.payloads.ClientPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public class LogoutSuccessPayload extends ClientPayload {
    @Override
    public void executePayload() {
        clientManager.setLoggedIn(false);
        clientManager.setPlayer(null);
    }
}
