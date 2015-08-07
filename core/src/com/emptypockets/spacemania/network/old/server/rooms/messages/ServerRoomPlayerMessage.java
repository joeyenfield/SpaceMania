package com.emptypockets.spacemania.network.old.server.rooms.messages;

import com.emptypockets.spacemania.network.old.client.rooms.messages.ClientRoomMessage;
import com.emptypockets.spacemania.network.old.server.player.ServerPlayer;

/**
 * Created by jenfield on 15/05/2015.
 */
public abstract class ServerRoomPlayerMessage<DST_MSG extends ClientRoomMessage> extends ServerRoomMessage<DST_MSG> {
    ServerPlayer serverPlayer;

    public void setServerPlayer(ServerPlayer serverPlayer) {
        this.serverPlayer = serverPlayer;
    }

    public ServerPlayer getServerPlayer() {
        return serverPlayer;
    }

    @Override
    public void reset() {
        serverPlayer = null;
    }
}
