package com.emptypockets.spacemania.network.old.server.rooms.messages;

import com.badlogic.gdx.utils.Pool;
import com.emptypockets.spacemania.network.old.client.rooms.messages.ClientRoomMessage;

/**
 * Created by jenfield on 15/05/2015.
 */
public abstract class ServerRoomMessage<MSG extends ClientRoomMessage> implements Pool.Poolable {
    public abstract MSG createClientMessage();

    @Override
    public void reset() {
    }
}
