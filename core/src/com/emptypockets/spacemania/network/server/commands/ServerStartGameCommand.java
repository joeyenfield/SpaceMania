package com.emptypockets.spacemania.network.server.commands;

import com.emptypockets.spacemania.engine.entityManager.BoundedEntityManager;
import com.emptypockets.spacemania.network.server.ServerManager;

/**
 * Created by jenfield on 11/05/2015.
 */
public class ServerStartGameCommand extends ServerCommand {
    public ServerStartGameCommand(ServerManager server) {
        super("startgame", server);
    }

    @Override
    public void exec(String args) {
        server.getEngine().setupTestData();
        server.startGame();
    }
}
