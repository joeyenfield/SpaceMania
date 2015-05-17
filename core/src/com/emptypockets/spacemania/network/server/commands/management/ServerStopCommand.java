package com.emptypockets.spacemania.network.server.commands.management;

import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.commands.ServerCommand;

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
