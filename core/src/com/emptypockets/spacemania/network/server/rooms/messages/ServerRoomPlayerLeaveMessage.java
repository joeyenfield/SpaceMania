package com.emptypockets.spacemania.network.server.rooms.messages;

import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomPlayerLeaveMessage;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ServerRoomPlayerLeaveMessage extends ServerRoomPlayerMessage<ClientRoomPlayerLeaveMessage> {

    public ServerRoomPlayerLeaveMessage(){
    }

    @Override
    public ClientRoomPlayerLeaveMessage createClientMessage() {
        ClientRoomPlayerLeaveMessage clientMessage = Pools.obtain(ClientRoomPlayerLeaveMessage.class);
        ClientPlayer player = Pools.obtain(ClientPlayer.class);
        player.read(getServerPlayer());
        clientMessage.setPlayer(player);
        return clientMessage;
    }
}
