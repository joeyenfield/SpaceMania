package com.emptypockets.spacemania.network.server.engine;


import com.emptypockets.spacemania.engine.players.Player;
import com.emptypockets.spacemania.network.server.ClientConnection;

/**
 * Created by jenfield on 12/05/2015.
 */
public class ServerPlayer extends Player {
    ServerGameRoom room;
    ClientConnection connection;

    public ServerGameRoom getRoom() {
        return room;
    }

    public void setRoom(ServerGameRoom room) {
        this.room = room;
    }

    public void setConnection(ClientConnection connection) {
        this.connection = connection;
    }

    public ClientConnection getConnection() {
        return connection;
    }
}
