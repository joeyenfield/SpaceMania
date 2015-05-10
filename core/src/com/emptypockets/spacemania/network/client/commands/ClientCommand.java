package com.emptypockets.spacemania.network.client.commands;


import com.emptypockets.spacemania.command.Command;
import com.emptypockets.spacemania.network.client.ClientManager;

public abstract class ClientCommand extends Command {

	ClientManager client;
	
	public ClientCommand(String name, ClientManager client) {
		super(name);
		this.client = client;
	}

}
