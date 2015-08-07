package com.emptypockets.spacemania.network.old.server.commands.management;

import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.old.server.commands.ServerCommand;

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
