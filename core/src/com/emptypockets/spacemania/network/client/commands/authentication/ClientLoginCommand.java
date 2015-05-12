package com.emptypockets.spacemania.network.client.commands.authentication;


import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

public class ClientLoginCommand extends ClientCommand {

	public ClientLoginCommand(ClientManager client) {
		super("login", client);
		setDescription("Attempts to login to a remote server : login {username}");
	}

	@Override
	public void exec(String data) {
		client.serverLogin(data);
	}
}
