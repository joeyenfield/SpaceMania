package com.emptypockets.spacemania.network.old.server.commands.management;

import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.old.server.commands.ServerCommand;

public class ServerStatusCommand extends ServerCommand {

    public ServerStatusCommand(ServerManager nodes) {
        super("status", nodes);
        setDescription("Logs the status of the current server : status");
    }

    @Override
    public void exec(String data) {
        server.logStatus();
    }
}
