package com.emptypockets.spacemania.network.client.commands.settings;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

public class ClientSetGridSizeCommand extends ClientCommand {

	public ClientSetGridSizeCommand(ClientManager client) {
		super("gridsize", client);
		setDescription("Changes the size of the grid currently being rendered - gridsize 100 100");
	}

	@Override
	public void exec(String args) {
		String[] data = args.toLowerCase().split(" ");
		if (data.length != 2) {
			getConsole().println("Two numbers are needed");
		} else {
			try {
				int sizeX = Integer.parseInt(data[0]);
				int sizeY = Integer.parseInt(data[1]);
				client.getEngine().getGridData().setSize(sizeX, sizeY);
			} catch (Throwable t) {
				getConsole().println("Error with command : " + t.getLocalizedMessage());
				getConsole().error(t);
			}
		}
	}

}
