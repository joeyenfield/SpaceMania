package com.emptypockets.spacemania.network.old.client.commands.rooms;

import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;
import com.emptypockets.spacemania.network.old.client.rooms.ClientRoom;

/**
 * Created by jenfield on 16/05/2015.
 */
public class ClientRoomCommand extends ClientCommand{
    public ClientRoomCommand(ClientManager client) {
        super("room", client);
        setDescription("Print your current room status");
    }

    @Override
    public void exec(String args) {
        ClientRoom room = client.getCurrentRoom();
        if(room == null){
        	client.getConsole().println("Not in a room");
        }else{
            room.logStatus(client.getConsole());
        }
    }
}
