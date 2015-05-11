package com.emptypockets.spacemania.network;


import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.connection.ClientConnectCommand;
import com.emptypockets.spacemania.network.client.commands.ClientLoginCommand;
import com.emptypockets.spacemania.network.client.commands.ClientLogoutCommand;
import com.emptypockets.spacemania.network.client.commands.ClientSearchCommand;
import com.emptypockets.spacemania.network.client.commands.ClientServerCommand;
import com.emptypockets.spacemania.network.client.commands.ClientStatusCommand;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.commands.ServerStartCommand;
import com.emptypockets.spacemania.network.server.commands.ServerStatusCommand;
import com.emptypockets.spacemania.network.server.commands.ServerStopCommand;
import com.emptypockets.spacemania.network.server.commands.ServerUsersCommand;

public class CommandService {

	public static void registerServer(ServerManager server) {
		server.getCommand().registerCommand(new ServerStartCommand(server));
		server.getCommand().registerCommand(new ServerStopCommand(server));
		server.getCommand().registerCommand(new ServerStatusCommand(server));
		server.getCommand().registerCommand(new ServerUsersCommand(server));
	}

	public static void registerClient(ClientManager client) {
		client.getCommand().registerCommand(new ClientConnectCommand(client));
		client.getCommand().registerCommand(new ClientLoginCommand(client));
		client.getCommand().registerCommand(new ClientLogoutCommand(client));
		client.getCommand().registerCommand(new ClientSearchCommand(client));
		client.getCommand().registerCommand(new ClientServerCommand(client));
		client.getCommand().registerCommand(new ClientStatusCommand(client));
		
		client.getCommand().pushHistory("connect 54.217.240.178,8080,8081");
		client.getCommand().pushHistory("connect localhost,8080,8081");
	}
}

