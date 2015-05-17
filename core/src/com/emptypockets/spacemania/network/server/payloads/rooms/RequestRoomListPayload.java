package com.emptypockets.spacemania.network.server.payloads.rooms;

import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.server.ClientConnection;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.server.rooms.ServerRoom;
import com.emptypockets.spacemania.network.transport.ComsType;

/**
 * Created by jenfield on 16/05/2015.
 */
public class RequestRoomListPayload extends ServerPayload {
    @Override
    public void executePayload(ClientConnection clientConnection, ServerManager serverManager) {
        final StringBuilder roomList = new StringBuilder();
        roomList.append("Server Rooms \n");
        serverManager.getRoomManager().process(new SingleProcessor<ServerRoom>() {
            @Override
            public void process(ServerRoom entity) {
                roomList.append(entity.getName()+" - ["+entity.getPlayerCount()+" / "+entity.getMaxPlayers()+"]\n");
            }
        });

        NotifyClientPayload payload = Pools.obtain(NotifyClientPayload.class);
        payload.setMessage(roomList.toString());
        payload.setComsType(ComsType.TCP);
        clientConnection.send(payload);
        Pools.free(payload);
    }
}
