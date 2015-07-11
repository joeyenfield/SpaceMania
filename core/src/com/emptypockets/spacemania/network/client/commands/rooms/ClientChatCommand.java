package com.emptypockets.spacemania.network.client.commands.rooms;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

/**
 * Created by jenfield on 16/05/2015.
 */
public class ClientChatCommand extends ClientCommand {
    public ClientChatCommand(ClientManager client) {
        super("chat", client);
        setDescription("Send a chat message to your current room : chat {message}");
    }

    @Override
    public void exec(String args) {
        client.sendChatMessage(args);
    }
}
