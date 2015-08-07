package com.emptypockets.spacemania.network.old.client.payloads.authentication;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.old.client.player.MyPlayer;

/**
 * Created by jenfield on 11/05/2015.
 */
public class LoginSuccessResponsePayload extends ClientPayload {
    String username;
    int playerId;


    @Override
    public void executePayload(ClientManager clientManager) {
    	clientManager.getConsole().println("Login Response : Successfully Logged in.");
        clientManager.setLoggedIn(true);
        MyPlayer clientPlayer = new MyPlayer();
        clientPlayer.setId(playerId);
        clientPlayer.setUsername(username);
        clientManager.setPlayer(clientPlayer);
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
