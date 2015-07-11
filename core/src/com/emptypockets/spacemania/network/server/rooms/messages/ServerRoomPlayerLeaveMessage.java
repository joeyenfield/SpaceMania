package com.emptypockets.spacemania.network.server.rooms.messages;

import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomPlayerLeaveMessage;
import com.emptypockets.spacemania.utils.PoolsManager;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ServerRoomPlayerLeaveMessage extends ServerRoomPlayerMessage<ClientRoomPlayerLeaveMessage> {

    public ServerRoomPlayerLeaveMessage(){
    }

    @Override
    public ClientRoomPlayerLeaveMessage createClientMessage() {
        ClientRoomPlayerLeaveMessage clientMessage = PoolsManager.obtain(ClientRoomPlayerLeaveMessage.class);
        ClientPlayer player = PoolsManager.obtain(ClientPlayer.class);
        player.read(getServerPlayer());
        clientMessage.setPlayer(player);
        return clientMessage;
    }
}
