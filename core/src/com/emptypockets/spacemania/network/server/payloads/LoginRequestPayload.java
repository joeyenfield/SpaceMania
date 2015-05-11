package com.emptypockets.spacemania.network.server.payloads;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.client.payloads.LoginFailedResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.LoginSuccessResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public class LoginRequestPayload extends ServerPayload {
    String username;

    @Override
    public void executePayload() {
        if (clientConnection.isLoggedIn()) {
            //Tell User to logout
            NotifyClientPayload resp = new NotifyClientPayload();
            resp.setMessage("You are already logged in as [" + clientConnection.getUsername() + "] - Log out first");
            clientConnection.sendTCP(resp);
        } else if (serverManager.isUserConnected(getUsername()) == null) {
            //Login user
            clientConnection.setUsername(getUsername());
            clientConnection.setLoggedIn(true);

            serverManager.clientJoin(getUsername());

            LoginSuccessResponsePayload resp = new LoginSuccessResponsePayload();
            resp.setUsername(getUsername());
            clientConnection.sendTCP(resp);
        } else {
            Console.println("User [" + getUsername() + "] is already connected");

            LoginFailedResponsePayload resp = new LoginFailedResponsePayload();
            resp.setErrorMessage("User already logged in");
            clientConnection.sendTCP(resp);
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void reset() {
        super.reset();
        this.username = null;
    }
}
