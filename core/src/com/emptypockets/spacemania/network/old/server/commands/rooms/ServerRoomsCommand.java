package com.emptypockets.spacemania.network.old.server.commands.rooms;

import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.old.server.commands.ServerCommand;
import com.emptypockets.spacemania.network.old.server.rooms.ServerRoom;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ServerRoomsCommand extends ServerCommand {
    public ServerRoomsCommand(ServerManager server) {
        super("rooms", server);
        setDescription("Logs all the rooms that are currently on the server");
    }

    @Override
    public void exec(String args) {
    	server.getConsole().println("Server Rooms ["+server.getRoomManager().getSize()+"]");
        server.getRoomManager().process(new SingleProcessor<ServerRoom>() {
            @Override
            public void process(ServerRoom room) {
            	server.getConsole().println("Room ["+room.getId()+"] - "+room.getName()+" : ["+room.getPlayerCount()+" / "+room.getMaxPlayers()+"]");
            }
        });
    }
}
