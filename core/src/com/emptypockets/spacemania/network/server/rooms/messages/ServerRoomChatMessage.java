package com.emptypockets.spacemania.network.server.rooms.messages;

import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomChatMessage;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomMessage;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

import java.text.DateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * Created by jenfield on 16/05/2015.
 */
public class ServerRoomChatMessage extends ServerRoomMessage<ClientRoomChatMessage> {
    long timestamp;
    String message;
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public ClientRoomChatMessage createClientMessage() {
        ClientRoomChatMessage result = Pools.obtain(ClientRoomChatMessage.class);
        result.setMessage(getMessage());
        result.setMessageTime(timestamp);
        result.setUsername(username);


        return result;
    }


}
