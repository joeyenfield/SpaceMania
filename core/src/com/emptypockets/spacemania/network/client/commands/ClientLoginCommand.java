package com.emptypockets.spacemania.network.client.commands;


import com.emptypockets.spacemania.network.client.ClientManager;

public class ClientLoginCommand extends ClientCommand {

	public ClientLoginCommand(ClientManager client) {
		super("login", client);
		setDescription("Attempts to login to a remote server : login {username}");
	}

	@Override
	public void exec(String data) {
		client.setUsername(data);
		client.serverLogin();
	}
}
