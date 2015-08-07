package com.emptypockets.spacemania.network.old.client.commands.settings;

import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;

public class ClientSetBufferCommand extends ClientCommand {

	public ClientSetBufferCommand(ClientManager client) {
		super("buffer", client);
		setDescription("Sets the buffer size");
	}

	@Override
	public void exec(String args) {
		String[] data = args.toLowerCase().split(" ");
		for(int i = 0; i < data.length; i++){
			getConsole().println("Two values are needed");
		}
	}
}
