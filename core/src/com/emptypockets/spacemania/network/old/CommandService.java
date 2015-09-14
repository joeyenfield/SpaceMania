package com.emptypockets.spacemania.network.old;


import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientHostCommand;
import com.emptypockets.spacemania.network.old.client.commands.ClientStartCommand;
import com.emptypockets.spacemania.network.old.client.commands.ClientStatusCommand;
import com.emptypockets.spacemania.network.old.client.commands.authentication.ClientLoginCommand;
import com.emptypockets.spacemania.network.old.client.commands.authentication.ClientLogoutCommand;
import com.emptypockets.spacemania.network.old.client.commands.connection.ClientConnectCommand;
import com.emptypockets.spacemania.network.old.client.commands.connection.ClientDisconnectCommand;
import com.emptypockets.spacemania.network.old.client.commands.connection.ClientIpCommand;
import com.emptypockets.spacemania.network.old.client.commands.connection.ClientPingCommand;
import com.emptypockets.spacemania.network.old.client.commands.connection.ClientSearchCommand;
import com.emptypockets.spacemania.network.old.client.commands.rooms.ClientChatCommand;
import com.emptypockets.spacemania.network.old.client.commands.rooms.ClientCreateRoomCommand;
import com.emptypockets.spacemania.network.old.client.commands.rooms.ClientJoinRoomCommand;
import com.emptypockets.spacemania.network.old.client.commands.rooms.ClientListRoomsCommand;
import com.emptypockets.spacemania.network.old.client.commands.rooms.ClientLobbyCommand;
import com.emptypockets.spacemania.network.old.client.commands.rooms.ClientRoomCommand;
import com.emptypockets.spacemania.network.old.client.commands.rooms.ClientSpawnCommand;
import com.emptypockets.spacemania.network.old.client.commands.settings.ClientSetCommand;
import com.emptypockets.spacemania.network.old.client.commands.settings.ClientSetGridRenderCommand;
import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.old.server.commands.management.ServerPingCommand;
import com.emptypockets.spacemania.network.old.server.commands.management.ServerStartCommand;
import com.emptypockets.spacemania.network.old.server.commands.management.ServerStatusCommand;
import com.emptypockets.spacemania.network.old.server.commands.management.ServerStopCommand;
import com.emptypockets.spacemania.network.old.server.commands.management.ServerUsersCommand;
import com.emptypockets.spacemania.network.old.server.commands.rooms.ServerRoomStatusCommand;
import com.emptypockets.spacemania.network.old.server.commands.rooms.ServerRoomsCommand;

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
    	client.getCommand().registerCommand(new ClientIpCommand(client));
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
        client.getCommand().registerCommand(new ClientSetCommand(client));
        client.getCommand().registerCommand(new ClientSetGridRenderCommand(client));
        client.getCommand().registerCommand(new ClientSpawnCommand(client));

    }
}
