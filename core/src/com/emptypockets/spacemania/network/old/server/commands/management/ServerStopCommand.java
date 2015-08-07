package com.emptypockets.spacemania.network.old.server.commands.management;

import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.old.server.commands.ServerCommand;

public class ServerStopCommand extends ServerCommand {

    public ServerStopCommand(ServerManager server) {
        super("stop", server);
        setDescription("Stops a currently running server : stop");
    }

    @Override
    public void exec(String data) {
        server.stop();
    }
}
