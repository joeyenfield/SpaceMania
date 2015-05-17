package com.emptypockets.spacemania.network.server.rooms.messages;

import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomPlayerJoinMessage;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ServerRoomPlayerJoinMessage extends ServerRoomPlayerMessage<ClientRoomPlayerJoinMessage> {
    public ServerRoomPlayerJoinMessage() {
    }

    @Override
    public ClientRoomPlayerJoinMessage createClientMessage() {
        ClientRoomPlayerJoinMessage clientMessage = Pools.obtain(ClientRoomPlayerJoinMessage.class);
        ClientPlayer player = Pools.obtain(ClientPlayer.class);
        player.read(getServerPlayer());
        clientMessage.setPlayer(player);
        return clientMessage;
    }
}
