package com.emptypockets.spacemania.network.old.client.commands;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.server.ServerManager;

public class ClientHostCommand extends ClientCommand {
    ServerManager manager;

    public ClientHostCommand(ClientManager client) {
        super("host", client);
        setDescription("This issues commands to the clients local server (see host help) : host [arg] ");
    }

    @Override
    public void exec(String data) {
        if (manager == null) {
            manager = new ServerManager(client.getConsole());
        }
        manager.getCommand().processCommand(data);

    }

}