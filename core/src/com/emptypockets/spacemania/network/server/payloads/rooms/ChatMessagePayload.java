package com.emptypockets.spacemania.network.server.payloads.rooms;

import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;

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
        if(clientConnection.isLoggedIn()){
            if(clientConnection.getPlayer() != null && clientConnection.getPlayer().isInRoom()){
                serverManager.chatRecieved(clientConnection.getPlayer(), getMessage());
               }else{
                NotifyClientPayload payload = new NotifyClientPayload();
                payload.setMessage("You are not in any room");
                clientConnection.send(payload);
            }
        }else{
            NotifyClientPayload payload = new NotifyClientPayload();
            payload.setMessage("You must be logged in to chat");
            clientConnection.send(payload);
        }
    }
}
