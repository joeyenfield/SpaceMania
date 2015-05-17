package com.emptypockets.spacemania.network.server.payloads.rooms;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.client.payloads.rooms.JoinRoomSuccessPayload;
import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.exceptions.TooManyPlayersException;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.server.rooms.ServerRoom;

/**
 * Created by jenfield on 16/05/2015.
 */
public class CreateRoomRequestPayload extends ServerPayload {
    String roomName;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public void executePayload(ClientConnection clientConnection, ServerManager serverManager) {
        if (clientConnection.isConnected()) {
            if (clientConnection.getPlayer() != null) {
                try {
                    ServerRoom room = serverManager.createRoom(clientConnection.getPlayer(), roomName);
                    serverManager.joinRoom(room, clientConnection.getPlayer());
                    room.updateClientRoom();

                    JoinRoomSuccessPayload payload = new JoinRoomSuccessPayload();
                    payload.setRoom(room.getClientRoom());
                    clientConnection.send(payload);

                } catch (TooManyPlayersException e) {
                    NotifyClientPayload payload = new NotifyClientPayload();
                    payload.setMessage("Could not create room");
                    clientConnection.send(payload);
                }
            } else {
                NotifyClientPayload payload = new NotifyClientPayload();
                payload.setMessage("You must be logged in to create a room");
                clientConnection.send(payload);
            }
        } else {
        	serverManager.getConsole().println("Room Creation failed");
        }
    }
}
