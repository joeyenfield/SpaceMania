package com.emptypockets.spacemania.network.server.payloads.engine;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.client.engine.ClientPlayer;
import com.emptypockets.spacemania.network.server.engine.ServerPlayer;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;

/**
 * Created by jenfield on 11/05/2015.
 */
public class PlayerStatePayload extends ServerPayload {

    Vector2 movement;
    Vector2 shoot;

    public PlayerStatePayload() {
        movement = new Vector2();
        shoot = new Vector2();
    }

    public void readPlayer(ClientPlayer player) {
        movement.set(player.getMovement());
        shoot.set(player.getShoot());
    }

    @Override
    public void executePayload() {
        ServerPlayer player = ((ServerPlayer) clientConnection.getPlayer());
        if (player != null) {
            player.getMovement().set(movement);
            player.getShoot().set(shoot);
        }
    }
}
