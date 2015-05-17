package com.emptypockets.spacemania.network.client.commands.rooms;


import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;
import com.emptypockets.spacemania.network.server.payloads.rooms.CreateRoomRequestPayload;

public class ClientCreateRoomCommand extends ClientCommand {

    public ClientCreateRoomCommand(ClientManager client) {
        super("createroom", client);
        setDescription("Attempts to create a room on the server: createroom {roomname}");
    }

    @Override
    public void exec(String data) {;
        client.createRoom(data);
    }
}
