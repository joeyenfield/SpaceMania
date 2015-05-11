package com.emptypockets.spacemania.network.client.payloads;

import com.emptypockets.spacemania.console.Console;

/**
 * Created by jenfield on 11/05/2015.
 */
public class LoginSuccessResponsePayload extends ClientPayload {
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void executePayload() {
        Console.println("Logout Response : Successfully Logged in.");
        getClientManager().setLoggedIn(true);
        getClientManager().setUsername(username);
    }

    @Override
    public void reset() {
        super.reset();
        username = null;
    }
}
