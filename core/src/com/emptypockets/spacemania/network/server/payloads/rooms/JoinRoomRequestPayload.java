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
        if (clientConnection.isConnected() && clientConnection.isLoggedIn()) {
            ServerRoom room = serverManager.getRoomManager().findRoomByName(roomName);
            if(room != null) {
                if(clientConnection.getPlayer().getCurrentRoom().equals(room)){
                    NotifyClientPayload payload = Pools.obtain(NotifyClientPayload.class);
                    payload.setMessage("You are already connected to this room");
                    payload.setComsType(ComsType.TCP);
                    clientConnection.send(payload);
                    Pools.free(payload);
                }else {
                    try {
                        serverManager.joinRoom(serverManager.getLobbyRoom(), clientConnection.getPlayer());
                        JoinRoomSuccessPayload payload = Pools.obtain(JoinRoomSuccessPayload.class);
                        payload.setRoom(serverManager.getLobbyRoom().getClientRoom());
                        payload.setComsType(ComsType.TCP);
                        clientConnection.send(payload);
                        Pools.free(payload);
                    } catch (TooManyPlayersException e) {
                        NotifyClientPayload payload = Pools.obtain(NotifyClientPayload.class);
                        payload.setMessage("Could not connect to lobby as it was full");
                        payload.setComsType(ComsType.TCP);
                        clientConnection.send(payload);
                        Pools.free(payload);
                    }
                }
            }else{

                NotifyClientPayload payload = Pools.obtain(NotifyClientPayload.class);
                payload.setMessage("No room found with name ["+roomName+"]");
                payload.setComsType(ComsType.TCP);
                clientConnection.send(payload);
                Pools.free(payload);
            }
        } else {
            NotifyClientPayload payload = Pools.obtain(NotifyClientPayload.class);
            payload.setMessage("You are not logged in, please login first");
            payload.setComsType(ComsType.TCP);
            clientConnection.send(payload);
            Pools.free(payload);
        }
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
