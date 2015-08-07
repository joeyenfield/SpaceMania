package com.emptypockets.spacemania.network.old.server.payloads.rooms;

import com.emptypockets.spacemania.network.old.server.ClientConnection;
import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.old.server.payloads.ServerPayload;

/**
 * Created by jenfield on 16/05/2015.
 */
public class ChatMessagePayload extends ServerPayload {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void executePayload(ClientConnection clientConnection, ServerManager serverManager) {
       serverManager.chatRecieved(clientConnection, message);
    }
}
