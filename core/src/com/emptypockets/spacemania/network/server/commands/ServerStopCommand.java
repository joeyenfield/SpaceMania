package com.emptypockets.spacemania.network.server.commands;

import com.emptypockets.spacemania.network.server.ServerManager;

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
