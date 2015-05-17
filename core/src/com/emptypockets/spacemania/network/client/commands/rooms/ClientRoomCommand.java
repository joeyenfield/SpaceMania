package com.emptypockets.spacemania.network.client.commands.rooms;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;

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
            Console.println("Not in a room");
        }else{
            room.logStatus();
        }
    }
}
