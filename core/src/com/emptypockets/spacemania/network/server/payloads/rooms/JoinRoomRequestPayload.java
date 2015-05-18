package com.emptypockets.spacemania.network.server.payloads.rooms;

import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.client.payloads.rooms.JoinRoomSuccessPayload;
import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.exceptions.TooManyPlayersException;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.server.rooms.ServerRoom;
import com.emptypockets.spacemania.network.transport.ComsType;

/**
 * Created by jenfield on 16/05/2015.
 */
public class JoinRoomRequestPayload extends ServerPayload{
    String roomName;

    @Override
    public void executePayload(ClientConnection clientConnection, ServerManager serverManager) {
        serverManager.joinRoom(clientConnection, roomName);
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
