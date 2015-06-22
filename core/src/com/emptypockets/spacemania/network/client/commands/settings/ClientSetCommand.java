package com.emptypockets.spacemania.network.client.commands.settings;

import com.emptypockets.spacemania.commandLine.CommandLine;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

public class ClientSetCommand extends ClientCommand {

	CommandLine commands;
	public ClientSetCommand(ClientManager client) {
		super("set", client);
		setDescription("Allows settings to be changed dynamically. User set help to get info of possible commands");
		commands = new CommandLine(client.getConsole());
		commands.registerCommand(new ClientSetGridSizeCommand(client));
		commands.registerCommand(new ClientSetGridRenderCommand(client));
		commands.registerCommand(new ClientSetGridCommand(client));
		commands.registerCommand(new ClientSetParticlesCommand(client));
		commands.registerCommand(new ClientSetRoomSizeCommand(client));
	}

	@Override
	public void exec(String commandString) {
		commands.processCommand(commandString);
	}

}
