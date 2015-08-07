package com.emptypockets.spacemania.network.old.client.rooms.messages;

import com.emptypockets.spacemania.network.old.client.player.ClientPlayer;

/**
 * Created by jenfield on 15/05/2015.
 */
public abstract class ClientRoomPlayerMessage extends ClientRoomMessage{
    ClientPlayer player;

    public ClientPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ClientPlayer player) {
        this.player = player;
    }

    @Override
    public void reset() {
        super.reset();
        player = null;
    }
}
