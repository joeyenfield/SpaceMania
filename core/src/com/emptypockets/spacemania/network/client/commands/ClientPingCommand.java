package com.emptypockets.spacemania.network.client.commands;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.commands.ServerCommand;

/**
 * Created by jenfield on 13/05/2015.
 */
public class ClientPingCommand extends ClientCommand {
    public ClientPingCommand(ClientManager client) {
        super("ping", client);
        setDescription("Request ping from server : ping");
    }

    @Override
    public void exec(String args) {
        client.updatePing();
    }
}
