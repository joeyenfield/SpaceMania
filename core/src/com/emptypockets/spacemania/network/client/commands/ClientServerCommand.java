package com.emptypockets.spacemania.network.client.commands;

import com.emptypockets.spacemania.network.client.ClientManager;

public class ClientServerCommand extends ClientCommand {

    public ClientServerCommand(ClientManager client) {
        super("server", client);
        setDescription("This issues commands to the clients local server (see server help) : server [arg] ");
    }

    @Override
    public void exec(String data) {
        if (data != null && data.startsWith("setup")) {
            client.setupServer();
        } else {
            client.getServerManager().getCommand().processCommand(data);
        }
    }

}
