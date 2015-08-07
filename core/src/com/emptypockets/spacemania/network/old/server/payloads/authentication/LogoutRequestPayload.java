package com.emptypockets.spacemania.network.old.server.payloads.authentication;


import com.emptypockets.spacemania.network.old.server.ClientConnection;
import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.old.server.payloads.ServerPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public class LogoutRequestPayload extends ServerPayload {
    @Override
    public void executePayload(ClientConnection clientConnection, ServerManager serverManager) {
    	serverManager.logout(clientConnection);
    }
}
