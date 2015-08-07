package com.emptypockets.spacemania.network.old.client.commands.rooms;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ClientJoinRoomCommand extends ClientCommand {
    public ClientJoinRoomCommand(ClientManager client) {
        super("joinroom", client);
        setDescription("Attempts to join a room by room name : joinroom {room name}");
    }

    @Override
    public void exec(String args) {
        client.joinRoom(args);
    }
}
