package com.emptypockets.spacemania.network.server.player;


import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.rooms.ServerRoom;

/**
 * Created by jenfield on 12/05/2015.
 */
public class ServerPlayer implements Disposable {
    int id;
    int ping;
    String username;
    ClientConnection clientConnection;
    ServerRoom currentRoom;
    ClientPlayer clientPlayer;

    public ServerPlayer(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        clientPlayer = new ClientPlayer();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Username[");
        result.append(username);
        result.append("] Ping [");
        result.append(ping);
        result.append("]");
        result.append("Room [");
        if(currentRoom == null){
            result.append("None");
        }else{
            result.append(currentRoom.getName());
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this){
            return true;
        }else if (o instanceof ServerPlayer){
            return ((ServerPlayer) o).getId()==getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isInRoom() {
        return currentRoom != null;
    }

    public void updateReturnTripTime() {
        clientConnection.updateReturnTripTime();
    }

    public void processIncommingPayloads() {
        clientConnection.processIncommingPayloads();
    }

    public void send(ClientPayload payload) {
        clientConnection.send(payload);
    }

    public void updateClientPlayer() {
        clientPlayer.read(this);
    }

    public ClientPlayer getClientPlayer() {
        return clientPlayer;
    }

    public ServerRoom getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(ServerRoom currentRoom) {
        this.currentRoom = currentRoom;
    }

    @Override
    public void dispose() {

    }
}
