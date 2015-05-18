package com.emptypockets.spacemania.network.server.payloads.authentication;

import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public class LoginRequestPayload extends ServerPayload {
    String username;
    String password;

    @Override
    public void executePayload( ClientConnection clientConnection, ServerManager serverManager) {
        serverManager.login(clientConnection, username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void reset() {
        super.reset();
        this.username = null;
        this.password = null;
    }
}
