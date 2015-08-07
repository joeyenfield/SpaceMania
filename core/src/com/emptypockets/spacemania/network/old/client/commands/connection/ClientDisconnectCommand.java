package com.emptypockets.spacemania.network.old.client.commands.connection;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;


public class ClientDisconnectCommand extends ClientCommand {

    public ClientDisconnectCommand(ClientManager client) {
        super("disconnect", client);
        setDescription("Disconnects from remote server : disconnect");
    }

    @Override
    public void exec(String data) {
        client.disconnect();
    }


}
