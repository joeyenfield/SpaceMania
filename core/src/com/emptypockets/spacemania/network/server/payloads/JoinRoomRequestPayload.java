package com.emptypockets.spacemania.network.server.payloads;

import com.emptypockets.spacemania.network.server.engine.ServerGameRoom;

/**
 * Created by jenfield on 12/05/2015.
 */
public class JoinRoomRequestPayload extends ServerPayload {
    int roomId;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public void executePayload() {
        ServerGameRoom room = serverManager.getRoomById(roomId);
    }
}
