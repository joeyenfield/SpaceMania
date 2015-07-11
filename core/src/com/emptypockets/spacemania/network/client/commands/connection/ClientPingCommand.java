package com.emptypockets.spacemania.network.client.commands.connection;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

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
