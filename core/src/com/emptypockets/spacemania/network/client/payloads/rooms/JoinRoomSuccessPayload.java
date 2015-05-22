package com.emptypockets.spacemania.network.client.payloads.rooms;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.engine.sync.EntityManagerSync;

/**
 * Created by jenfield on 15/05/2015.
 */
public class JoinRoomSuccessPayload extends ClientPayload {
    ClientRoom room;
	EntityManagerSync initialSync;

    public void setRoom(ClientRoom room){
        this.room = room;
    }
    
    public void setInitialSync(EntityManagerSync initialSync){
    	this.initialSync = initialSync;
    }
    @Override
    public void executePayload(ClientManager clientManager) {
        clientManager.setCurrentRoom(room);
        clientManager.getEngine().getEntityManager().clear();
        initialSync.setSyncTime(true);
        initialSync.writeToEngine(clientManager.getEngine());
    }

    @Override
    public void reset() {
        super.reset();
        room = null;
    }
}
