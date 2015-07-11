package com.emptypockets.spacemania.network.server.rooms.messages;

import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomPlayerJoinMessage;
import com.emptypockets.spacemania.utils.PoolsManager;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ServerRoomPlayerJoinMessage extends ServerRoomPlayerMessage<ClientRoomPlayerJoinMessage> {
    public ServerRoomPlayerJoinMessage() {
    }

    @Override
    public ClientRoomPlayerJoinMessage createClientMessage() {
        ClientRoomPlayerJoinMessage clientMessage = PoolsManager.obtain(ClientRoomPlayerJoinMessage.class);
        ClientPlayer player = PoolsManager.obtain(ClientPlayer.class);
        player.read(getServerPlayer());
        clientMessage.setPlayer(player);
        return clientMessage;
    }
}
