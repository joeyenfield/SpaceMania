package com.emptypockets.spacemania.network.server.rooms;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.CleanerProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.payloads.rooms.ClientRoomPayload;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.exceptions.TooManyPlayersException;
import com.emptypockets.spacemania.network.server.player.PlayerManager;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomChatMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomPlayerJoinMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomPlayerLeaveMessage;
import com.emptypockets.spacemania.network.transport.ComsType;

/**
 * Created by jenfield on 14/05/2015.
 */
public class ServerRoom implements Disposable {
    int id;
    String name;
    ServerManager manager;
    ServerPlayer host;
    ArrayListProcessor<ServerRoomMessage> messageManager;
    PlayerManager playerManager;
    ClientRoom clientRoom;

    public ServerRoom(ServerManager manager) {
        super();
        this.manager = manager;
        messageManager = new ArrayListProcessor<ServerRoomMessage>();
        playerManager = new PlayerManager();
        clientRoom = new ClientRoom();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this){
            return true;
        }else if (o instanceof ServerPlayer){
            return ((ServerPlayer) o).getId()==getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    public int getMaxPlayers() {
        return playerManager.getMaxPlayers();
    }

    public void setMaxPlayers(int maxPlayers) {
        playerManager.setMaxPlayers(maxPlayers);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return playerManager.isEmpty();
    }


    @Override
    public void dispose() {
        playerManager.clear();
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public int getPlayerCount() {
        return playerManager.getSize();
    }


    public void updateClientRoom() {
        clientRoom.read(this);
    }

    public ClientRoom getClientRoom() {
        return clientRoom;
    }

    public void update() {

    }

    public void broadcast() {
        //If no messages dont broadcast
        if (messageManager.getSize() == 0) {
            return;
        }
        final ClientRoomPayload payload = Pools.obtain(ClientRoomPayload.class);
        payload.setComsType(ComsType.TCP);
        payload.setRoomId(getId());
        messageManager.process(new CleanerProcessor<ServerRoomMessage>() {
            @Override
            public boolean shouldRemove(ServerRoomMessage entity) {
                payload.addMessage(entity.createClientMessage());
                Pools.free(entity);
                return true;
            }
        });
        getPlayerManager().process(new SingleProcessor<ServerPlayer>() {
            @Override
            public void process(ServerPlayer entity) {
                entity.send(payload);
            }
        });
        Pools.free(payload);
    }

    public void joinRoom(ServerPlayer player) throws TooManyPlayersException {
        playerManager.addPlayer(player);

        ServerRoomPlayerJoinMessage message = Pools.obtain(ServerRoomPlayerJoinMessage.class);
        message.setServerPlayer(player);
        messageManager.add(message);

        sendMessage(String.format("%s has joined the room", player.getUsername()));
    }

    public void leaveRoom(ServerPlayer player) {
        playerManager.removePlayer(player);

        ServerRoomPlayerLeaveMessage message = Pools.obtain(ServerRoomPlayerLeaveMessage.class);
        message.setServerPlayer(player);
        messageManager.add(message);

        sendMessage(String.format("%s has left the room", player.getUsername()));
    }

    public void sendMessage(String messageContent){
        sendMessage(messageContent, null);
    }
    public void sendMessage(String messageContent, String username){
        ServerRoomChatMessage message = Pools.obtain(ServerRoomChatMessage.class);
        message.setTimestamp(System.currentTimeMillis());
        message.setMessage(messageContent);
        message.setUsername(username);
        messageManager.add(message);
    }

    public ServerPlayer getHost() {
        return host;
    }

    public void setHost(ServerPlayer host) {
        this.host = host;
    }


}
