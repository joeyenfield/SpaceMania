package com.emptypockets.spacemania.network.old.client.commands.rooms;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ClientLobbyCommand extends ClientCommand {
    public ClientLobbyCommand(ClientManager client) {
        super("lobby", client);
        setDescription("Attempts to return to the lobby");
    }

    @Override
    public void exec(String args) {
        client.joinLobby();
    }
}