package com.emptypockets.spacemania.network.server.payloads.engine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.emptypockets.spacemania.engine.BaseEntity;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.server.Player;
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

    public void readPlayer(Touchpad movePad, Touchpad shootPad){
        movement.set(movePad.getKnobPercentX(), movePad.getKnobPercentY());
        shoot.set(shootPad.getKnobPercentX(),shootPad.getKnobPercentY());
    }
    @Override
    public void executePayload() {
        synchronized (serverManager.getEngine()) {
            Player player = serverManager.getEngine().getPlayerByName(clientConnection.getUsername());
            if(player != null){
                player.getMovement().set(movement);
                player.getShoot().set(shoot);
            }
        }
    }
}
