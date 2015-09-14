package com.emptypockets.spacemania.network.old.client.rooms.messages;

import com.badlogic.gdx.utils.Pool;
import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.rooms.ClientRoom;

/**
 * Created by jenfield on 15/05/2015.
 */
public abstract class ClientRoomMessage implements Pool.Poolable{

    public abstract void processMessage(ClientManager manager, ClientRoom room);


    @Override
    public void reset() {
    }
}