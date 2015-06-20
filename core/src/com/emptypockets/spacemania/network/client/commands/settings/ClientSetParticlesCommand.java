package com.emptypockets.spacemania.network.client.commands.settings;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

public class ClientSetParticlesCommand extends ClientCommand {

	public ClientSetParticlesCommand(ClientManager client) {
		super("particles", client);
		setDescription("Changes the max number of particles- particles 100");
	}

	@Override
	public void exec(String args) {
		String[] data = args.toLowerCase().split(" ");
		if (data.length != 1) {
			getConsole().println("A number is needed");
		} else {
			try {
				int number = Integer.parseInt(data[0]);
				client.getEngine().getParticleSystem().setMaxParticles(number);
			} catch (Throwable t) {
				getConsole().println("Error with command : " + t.getLocalizedMessage());
				getConsole().error(t);
			}
		}
	}

}
