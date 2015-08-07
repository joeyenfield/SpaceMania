package com.emptypockets.spacemania.network.old.server.commands.rooms;

import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.old.server.ServerManager;
import com.emptypockets.spacemania.network.old.server.commands.ServerCommand;
import com.emptypockets.spacemania.network.old.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.old.server.rooms.ServerRoom;

/**
 * Created by jenfield on 16/05/2015.
 */
public class ServerRoomStatusCommand extends ServerCommand {
    public ServerRoomStatusCommand(ServerManager server) {
        super("roomstatus", server);
        setDescription("Prints the status of the room : roomstatus {Room Id/Room Name}");
    }

    @Override
    public void exec(String args) {

        ServerRoom room = null;

        try {
            int roomId = Integer.parseInt(args);
            room =server.getRoomManager().findRoomById(roomId);
        }catch(Throwable t){
        }

        if(room == null){
            room = server.getRoomManager().findRoomByName(args);
        }

        server.getConsole().println("Room ["+room.getId()+"] - "+room.getName()+" : ["+room.getPlayerCount()+" / "+room.getMaxPlayers()+"]");
        room.getPlayerManager().process(new SingleProcessor<ServerPlayer>() {
            @Override
            public void process(ServerPlayer entity) {
            	server.getConsole().println("Player : "+entity);
            }
        });

    }
}
