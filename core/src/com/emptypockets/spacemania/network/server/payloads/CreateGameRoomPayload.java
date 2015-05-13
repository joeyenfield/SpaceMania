package com.emptypockets.spacemania.network.server.payloads;

import com.emptypockets.spacemania.network.server.engine.ServerGameRoom;

/**
 * Created by jenfield on 12/05/2015.
 */
public class CreateGameRoomPayload extends ServerPayload {
    String roomName;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public void executePayload() {
        serverManager.createRoom(clientConnection.getPlayer(), roomName);
    }
}
