package com.emptypockets.spacemania.network.client.commands.settings;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

public class ClientSetGridCommand extends ClientCommand {

	public ClientSetGridCommand(ClientManager client) {
		super("grid", client);
		setDescription("Changes the grid - 0 (static), 1 (dynamic)");
	}

	@Override
	public void exec(String args) {
		String[] data = args.toLowerCase().split(" ");
		if (data.length != 1) {
			getConsole().println("A number is needed");
		} else {
			try {
				int type = Integer.parseInt(data[0]);
				if (type == 0) {
					client.getEngine().setDynamicGrid(false);
				} else {
					client.getEngine().setDynamicGrid(true);
				}
			} catch (Throwable t) {
				getConsole().println("Error with command : " + t.getLocalizedMessage());
				getConsole().error(t);
			}
		}
	}


}
