package com.emptypockets.spacemania.network.old.client.rooms.messages;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.rooms.ClientRoom;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ClientRoomPlayerJoinMessage extends ClientRoomPlayerMessage{
    @Override
    public void processMessage(ClientManager manager, ClientRoom room) {
        room.addPlayer(getPlayer());
    }
}
