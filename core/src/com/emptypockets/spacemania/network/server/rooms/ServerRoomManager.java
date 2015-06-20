package com.emptypockets.spacemania.network.server.rooms;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.holders.ObjectProcessor;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jenfield on 13/05/2015.
 */
public class ServerRoomManager extends ObjectProcessor<ServerRoom> {
    HashMap<Integer, ServerRoom> roomData;
    ServerManager manager;
    int createdRoomCount = 0;

    public ServerRoomManager(ServerManager manager) {
        super();
        this.manager = manager;
        roomData = new HashMap<Integer, ServerRoom>();
    }

    public synchronized ServerRoom createNewRoom(ServerPlayer host, String roomName) {
        createdRoomCount++;

        //Set State
        ServerRoom room = new ServerRoom(manager);
        room.setId(createdRoomCount);
        room.setName(roomName);
        room.setBroadcastPeroid(manager.getRoomDefaultBroadcastTime());

        roomData.put(room.getId(), room);

        //Join the room
        room.setHost(host);
        return room;
    }

    @Override
    protected Iterator<ServerRoom> getIterator() {
        return roomData.values().iterator();
    }

    public synchronized int getSize() {
        return roomData.size();
    }

    public synchronized ServerRoom findRoomById(int roomId) {
        return roomData.get(roomId);
    }

    public synchronized ServerRoom findRoomByName(String name) {
        Iterator<ServerRoom> iterator = getIterator();
        while (iterator.hasNext()) {
            ServerRoom entity = iterator.next();
            if(entity.getName().equalsIgnoreCase(name)){
                return entity;
            }
        }
        return null;
    }


    public synchronized  void removeRoom(ServerRoom room){
        roomData.remove(room.getId());
    }
}
