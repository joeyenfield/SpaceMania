package com.emptypockets.spacemania.network.client.commands.administrator;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

/**
 * Created by jenfield on 14/05/2015.
 */
public class AdminClientCommand extends ClientCommand {
    public AdminClientCommand(ClientManager client) {
        super("admin", client);
        setDescription("Sends Administrator messages to the server : admin - status");
    }

    @Override
    public void exec(String args) {

    }
}
