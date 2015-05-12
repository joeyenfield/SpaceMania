package com.emptypockets.spacemania.network.client.commands.authentication;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

public class ClientLogoutCommand extends ClientCommand {

	public ClientLogoutCommand(ClientManager client) {
		super("logout", client);
		setDescription("Logs a user out of a server : logout");
	}

	@Override
	public void exec(String data) {
		client.serverLogout();
	}
}
