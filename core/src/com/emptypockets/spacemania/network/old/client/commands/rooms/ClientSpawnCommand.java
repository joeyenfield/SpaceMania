package com.emptypockets.spacemania.network.old.client.commands.rooms;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ClientSpawnCommand extends ClientCommand {
	public static final String COMMAND_TEXT = "spawn";
    public ClientSpawnCommand(ClientManager client) {
        super(COMMAND_TEXT, client);
        setDescription("Request a respawn from the server");
    }

    @Override
    public void exec(String args) {
        client.spawn();
    }
}