package com.emptypockets.spacemania.network.old.client.commands.connection;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;

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
