package com.emptypockets.spacemania.network.old.client.commands.rooms;


import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;

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
