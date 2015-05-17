package com.emptypockets.spacemania.network.server.payloads.authentication;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginFailedResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginSuccessResponsePayload;
import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.exceptions.TooManyPlayersException;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

/**
 * Created by jenfield on 11/05/2015.
 */
public class LoginRequestPayload extends ServerPayload {
    String username;
    String password;

    @Override
    public void executePayload( ClientConnection clientConnection, ServerManager serverManager) {
        if (clientConnection.isLoggedIn()) {
            //The user is already logged in - Tell User to logout
            NotifyClientPayload resp = new NotifyClientPayload();
            resp.setMessage("You are already logged in as [" + clientConnection.getPlayer().getUsername() + "] - Log out first");
            clientConnection.sendTCP(resp);
        } else if (serverManager.isUserConnected(getUsername())) {
            // The username is already in use;
            Console.println("User [" + getUsername() + "] is already connected");
            LoginFailedResponsePayload resp = new LoginFailedResponsePayload();
            resp.setErrorMessage("User already logged in");
            clientConnection.sendTCP(resp);
        }else{
            //Try to login
            try {
                ServerPlayer player = serverManager.clientLogin(clientConnection, username, password);
                //Login user
                clientConnection.setPlayer(player);
                clientConnection.setLoggedIn(true);

                LoginSuccessResponsePayload resp = new LoginSuccessResponsePayload();
                resp.setUsername(player.getUsername());
                resp.setPlayerId(player.getId());
                clientConnection.sendTCP(resp);
            } catch (TooManyPlayersException e) {
                LoginFailedResponsePayload resp = new LoginFailedResponsePayload();
                resp.setErrorMessage("Server currently full");
                clientConnection.sendTCP(resp);
            }
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
