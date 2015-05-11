package com.emptypockets.spacemania.network.server;

/**
 * Created by jenfield on 11/05/2015.
 */
public class Player {
    int playerId;
    String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
