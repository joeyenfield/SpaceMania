package com.emptypockets.spacemania.network.server.payloads.authentication;


import com.emptypockets.spacemania.network.client.payloads.authentication.LogoutSuccessPayload;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public class LogoutRequestPayload extends ServerPayload {
    @Override
    public void executePayload() {
        if(clientConnection.getLoggedIn()){
            serverManager.clientExit(clientConnection.getUsername());
            clientConnection.setUsername(null);
            clientConnection.setLoggedIn(false);

            LogoutSuccessPayload response = new LogoutSuccessPayload();
            clientConnection.sendTCP(response);
        }else{
            NotifyClientPayload resp = new NotifyClientPayload();
            resp.setMessage("Your not logged in");
            clientConnection.sendTCP(resp);
        }

    }
}
