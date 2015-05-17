package com.emptypockets.spacemania.network.client.payloads.rooms;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;

/**
 * Created by jenfield on 15/05/2015.
 */
public class JoinRoomSuccessPayload extends ClientPayload {
    ClientRoom room;

    public void setRoom(ClientRoom room){
        this.room = room;
    }
    @Override
    public void executePayload(ClientManager clientManager) {
        clientManager.setCurrentRoom(room);
    }

    @Override
    public void reset() {
        super.reset();
        room = null;
    }
}
