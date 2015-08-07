package com.emptypockets.spacemania.network.old.client.commands.settings;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;

public class ClientSetRoomSizeCommand extends ClientCommand {

	public ClientSetRoomSizeCommand(ClientManager client) {
		super("roomsize", client);
		setDescription("Changes the size of the grid currently being rendered - gridsize 100 100");
	}

	@Override
	public void exec(String args) {
		int size = Integer.parseInt(args);
		getClient().resizeRoom(size);
	}

}
