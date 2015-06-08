package com.emptypockets.spacemania.network;


import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.connection.ClientPingCommand;
import com.emptypockets.spacemania.network.client.commands.ClientHostCommand;
import com.emptypockets.spacemania.network.client.commands.ClientStartCommand;
import com.emptypockets.spacemania.network.client.commands.ClientStatusCommand;
import com.emptypockets.spacemania.network.client.commands.authentication.ClientLoginCommand;
import com.emptypockets.spacemania.network.client.commands.authentication.ClientLogoutCommand;
import com.emptypockets.spacemania.network.client.commands.connection.ClientConnectCommand;
import com.emptypockets.spacemania.network.client.commands.connection.ClientDisconnectCommand;
import com.emptypockets.spacemania.network.client.commands.connection.ClientSearchCommand;
import com.emptypockets.spacemania.network.client.commands.rooms.ClientChatCommand;
import com.emptypockets.spacemania.network.client.commands.rooms.ClientCreateRoomCommand;
import com.emptypockets.spacemania.network.client.commands.rooms.ClientListRoomsCommand;
import com.emptypockets.spacemania.network.client.commands.rooms.ClientLobbyCommand;
import com.emptypockets.spacemania.network.client.commands.rooms.ClientRoomCommand;
import com.emptypockets.spacemania.network.client.commands.rooms.ClientJoinRoomCommand;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.commands.management.ServerPingCommand;
import com.emptypockets.spacemania.network.server.commands.management.ServerStartCommand;
import com.emptypockets.spacemania.network.server.commands.management.ServerStatusCommand;
import com.emptypockets.spacemania.network.server.commands.management.ServerStopCommand;
import com.emptypockets.spacemania.network.server.commands.management.ServerUsersCommand;
import com.emptypockets.spacemania.network.server.commands.rooms.ServerRoomStatusCommand;
import com.emptypockets.spacemania.network.server.commands.rooms.ServerRoomsCommand;
import com.emptypockets.spacemania.network.server.payloads.rooms.ChatMessagePayload;

public class CommandService {

    public static void registerServerCommands(ServerManager server) {
        server.getCommand().registerCommand(new ServerStartCommand(server));
        server.getCommand().registerCommand(new ServerStopCommand(server));
        server.getCommand().registerCommand(new ServerStatusCommand(server));
        server.getCommand().registerCommand(new ServerUsersCommand(server));
        server.getCommand().registerCommand(new ServerPingCommand(server));
        server.getCommand().registerCommand(new ServerRoomsCommand(server));
        server.getCommand().registerCommand(new ServerRoomStatusCommand(server));
    }

    public static void registerClientCommands(ClientManager client) {
    	client.getCommand().registerCommand(new ClientStartCommand(client));
        client.getCommand().registerCommand(new ClientJoinRoomCommand(client));

        client.getCommand().registerCommand(new ClientConnectCommand(client));
        client.getCommand().registerCommand(new ClientLoginCommand(client));
        client.getCommand().registerCommand(new ClientLogoutCommand(client));
        client.getCommand().registerCommand(new ClientSearchCommand(client));
        client.getCommand().registerCommand(new ClientHostCommand(client));
        client.getCommand().registerCommand(new ClientStatusCommand(client));
        client.getCommand().registerCommand(new ClientDisconnectCommand(client));
        client.getCommand().registerCommand(new ClientPingCommand(client));
        client.getCommand().registerCommand(new ClientLobbyCommand(client));
        client.getCommand().registerCommand(new ClientRoomCommand(client));
        client.getCommand().registerCommand(new ClientCreateRoomCommand(client));
        client.getCommand().registerCommand(new ClientChatCommand(client));
        client.getCommand().registerCommand(new ClientListRoomsCommand(client));


    }
}

