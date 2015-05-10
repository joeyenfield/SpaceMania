package com.emptypockets.spacemania.network.server.commands;

import com.emptypockets.spacemania.network.server.ServerManager;

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
