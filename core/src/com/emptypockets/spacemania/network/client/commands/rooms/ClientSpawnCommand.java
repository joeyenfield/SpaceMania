package com.emptypockets.spacemania.network.client.commands.rooms;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ClientSpawnCommand extends ClientCommand {
    public ClientSpawnCommand(ClientManager client) {
        super("spawn", client);
        setDescription("Attempts to return to the lobby");
    }

    @Override
    public void exec(String args) {
        client.spawn();
    }
}