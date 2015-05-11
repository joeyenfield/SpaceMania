package com.emptypockets.spacemania.network.server.commands;

import com.emptypockets.spacemania.network.server.ServerManager;

/**
 * Created by jenfield on 11/05/2015.
 */
public class ServerUsersCommand extends ServerCommand {
    public ServerUsersCommand(ServerManager nodes) {
        super("users", nodes);
        setDescription("Logs all users on the server : users");
    }

    @Override
    public void exec(String data) {
        server.logUsers();
    }
}
