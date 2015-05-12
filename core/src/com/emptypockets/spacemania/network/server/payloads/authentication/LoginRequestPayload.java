package com.emptypockets.spacemania.network.server.payloads.authentication;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.engine.players.Player;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginFailedResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginSuccessResponsePayload;
import com.emptypockets.spacemania.network.server.engine.ServerPlayer;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public class LoginRequestPayload extends ServerPayload {
    String username;
    String password;

    @Override
    public void executePayload() {
        if (clientConnection.isLoggedIn()) {
            //Tell User to logout
            NotifyClientPayload resp = new NotifyClientPayload();
            resp.setMessage("You are already logged in as [" + clientConnection.getPlayer().getUsername() + "] - Log out first");
            clientConnection.sendTCP(resp);
        } else if (!serverManager.isUserConnected(getUsername())) {
            ServerPlayer player = serverManager.clientLogin(clientConnection, username, password);
            if (player != null) {
                //Login user
                clientConnection.setPlayer(player);
                clientConnection.setLoggedIn(true);

                LoginSuccessResponsePayload resp = new LoginSuccessResponsePayload();
                resp.setUsername(player.getUsername());
                resp.setPlayerId(player.getId());
                clientConnection.sendTCP(resp);
            } else {
                LoginFailedResponsePayload resp = new LoginFailedResponsePayload();
                resp.setErrorMessage("Login failed");
                clientConnection.sendTCP(resp);
            }
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
