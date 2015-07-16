package com.emptypockets.spacemania.network.client.commands;

import com.emptypockets.spacemania.network.client.ClientManager;

public class ClientStartCommand extends ClientCommand {
    public ClientStartCommand(ClientManager client) {
        super("start", client);
        setDescription("This issues commands to the clients local server (see host help) : host [arg] ");
    }

	@Override
	public void exec(String args) {
		client.getCommand().processCommand("host start;connect;login local;lobby;spawn");
	}

}
