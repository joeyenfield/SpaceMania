package com.emptypockets.spacemania.network.old.server.payloads.administrator;

import com.emptypockets.spacemania.network.old.server.ClientConnection;
import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.old.server.payloads.ServerPayload;

/**
 * Created by jenfield on 14/05/2015.
 */
public class AdminServerPayload extends ServerPayload {

    String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public void executePayload(ClientConnection clientConnection, ServerManager serverManager) {
        serverManager.getCommand().processCommand(command);
    }
}
