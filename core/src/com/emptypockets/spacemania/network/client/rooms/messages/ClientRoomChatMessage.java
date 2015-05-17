package com.emptypockets.spacemania.network.client.rooms.messages;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;

/**
 * Created by jenfield on 16/05/2015.
 */
public class ClientRoomChatMessage extends ClientRoomMessage {
    long messageTime;
    String message;
    String username;

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void processMessage(ClientManager manager, ClientRoom room) {
        room.chatMessage(manager.getConsole(),messageTime, username, message);
    }

    @Override
    public void reset() {
        super.reset();
        message = null;
        username = null;
    }
}
