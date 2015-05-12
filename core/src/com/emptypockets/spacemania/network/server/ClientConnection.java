package com.emptypockets.spacemania.network.server;

import com.emptypockets.spacemania.network.server.engine.ServerPlayer;
import com.esotericsoftware.kryonet.Connection;

public class ClientConnection extends Connection {
    ServerPlayer player;
    private boolean loggedIn;

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ServerPlayer player) {
        this.player = player;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
