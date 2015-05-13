package com.emptypockets.spacemania.network.client.commands;

import com.emptypockets.spacemania.network.client.ClientManager;

public class ClientStatusCommand extends ClientCommand {

    public ClientStatusCommand(ClientManager client) {
        super("status", client);
        setDescription("Echos the status of the client : status");
    }

    @Override
    public void exec(String data) {
        client.listStatus();
    }
}
