package com.emptypockets.spacemania.network.old.server.commands;

import com.emptypockets.spacemania.commandLine.Command;
import com.emptypockets.spacemania.network.old.server.ServerManager;

public abstract class ServerCommand extends Command {

    protected ServerManager server;

    public ServerCommand(String name, ServerManager server) {
        super(name);
        this.server = server;
    }

}