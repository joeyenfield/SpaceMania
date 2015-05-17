package com.emptypockets.spacemania.network.client.payloads.authentication;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public class LoginFailedResponsePayload extends ClientPayload {
    String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void executePayload(ClientManager clientManager) {
        Console.println("Login Failed : " + errorMessage);
        clientManager.setLoggedIn(false);
        clientManager.setPlayer(null);
    }

    @Override
    public void reset() {
        super.reset();
        errorMessage = null;
    }
}
