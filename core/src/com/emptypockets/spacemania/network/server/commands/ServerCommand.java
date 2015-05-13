package com.emptypockets.spacemania.network.server.commands;

import com.emptypockets.spacemania.commandLine.Command;
import com.emptypockets.spacemania.network.server.ServerManager;

public abstract class ServerCommand extends Command {

	ServerManager server;
	
	public ServerCommand(String name, ServerManager server) {
		super(name);
		this.server = server;
	}

}
