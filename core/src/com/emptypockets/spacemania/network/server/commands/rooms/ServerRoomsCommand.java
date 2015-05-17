package com.emptypockets.spacemania.network.server.commands.rooms;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.commands.ServerCommand;
import com.emptypockets.spacemania.network.server.rooms.ServerRoom;

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
        Console.println("Server Rooms ["+server.getRoomManager().getSize()+"]");
        server.getRoomManager().process(new SingleProcessor<ServerRoom>() {
            @Override
            public void process(ServerRoom room) {
                Console.println("Room ["+room.getId()+"] - "+room.getName()+" : ["+room.getPlayerCount()+" / "+room.getMaxPlayers()+"]");
            }
        });
    }
}
