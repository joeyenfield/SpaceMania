package com.emptypockets.spacemania.network.server;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by jenfield on 11/05/2015.
 */
public class Player {
    int playerId;
    String playerName;

    Vector2 movement;
    Vector2 shoot;

    public Player() {
        super();
        movement = new Vector2();
        shoot = new Vector2();

    }

    public Vector2 getShoot() {
        return shoot;
    }

    public void setShoot(Vector2 shoot) {
        this.shoot = shoot;
    }
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

    public Vector2 getMovement() {
        return movement;
    }

    public void setMovement(Vector2 movement) {
        this.movement = movement;
    }


}
