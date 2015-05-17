package com.emptypockets.spacemania.network.client.payloads.rooms;

import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomMessage;

import java.util.ArrayList;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ClientRoomPayload extends ClientPayload {
    int roomId;
    ArrayList<ClientRoomMessage> messages;

    public ClientRoomPayload(){
        messages = new ArrayList<ClientRoomMessage>();
    }

    public void addMessage(ClientRoomMessage message){
        messages.add(message);
    }

    public void setRoomId(int roomId){
        this.roomId = roomId;
    }
    @Override
    public void executePayload(ClientManager clientManager) {
        ClientRoom room = clientManager.getCurrentRoom();
        if(room != null){
            if(room.getId() == roomId){
                room.processMessages(messages);
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        roomId = -1;
        for(ClientRoomMessage message : messages){
            Class<? extends ClientRoomMessage> messageClass= message.getClass();
            Pools.free(message);
        }
        messages.clear();
    }
}
