package com.emptypockets.spacemania.network.client.commands.rooms;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

/**
 * Created by jenfield on 16/05/2015.
 */
public class ClientListRoomsCommand extends ClientCommand{

    public ClientListRoomsCommand(ClientManager client) {
        super("listrooms", client);
        setDescription("List the rooms currently on the server : listrooms");
    }

    @Override
    public void exec(String args) {
        client.requestServerRooms();
    }
}
