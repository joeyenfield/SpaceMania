package com.emptypockets.spacemania.network.client.payloads.authentication;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.client.engine.ClientPlayer;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public class LoginSuccessResponsePayload extends ClientPayload {
    String username;
    int playerId;



    @Override
    public void executePayload() {
        Console.println("Logout Response : Successfully Logged in.");
        getClientManager().setLoggedIn(true);

        ClientPlayer clientPlayer = new ClientPlayer();
        clientPlayer.setId(playerId);
        clientPlayer.setUsername(username);

        getClientManager().setPlayer(clientPlayer);
    }

    @Override
    public void reset() {
        super.reset();
        username = null;
        playerId = -1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
