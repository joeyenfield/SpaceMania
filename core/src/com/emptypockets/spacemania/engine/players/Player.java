package com.emptypockets.spacemania.engine.players;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by jenfield on 11/05/2015.
 */
public class Player {
    int id;
    String username;

    Vector2 movement;
    Vector2 shoot;
    long lastShootTime = 0;
    long shootSpeed = 1000;

    public Player() {
        super();
        movement = new Vector2();
        shoot = new Vector2();

    }

    public boolean canShoot(){
        return System.currentTimeMillis()-lastShootTime > shootSpeed;
    }

    public Vector2 getShoot() {
        return shoot;
    }

    public void setShoot(Vector2 shoot) {
        this.shoot = shoot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getShootSpeed() {
        return shootSpeed;
    }

    public void setShootSpeed(long shootSpeed) {
        this.shootSpeed = shootSpeed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Vector2 getMovement() {
        return movement;
    }

    public void setMovement(Vector2 movement) {
        this.movement = movement;
    }


}
