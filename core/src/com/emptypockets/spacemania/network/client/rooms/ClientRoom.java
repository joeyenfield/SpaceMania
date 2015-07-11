package com.emptypockets.spacemania.network.client.rooms;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.client.rooms.messages.ClientRoomMessage;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.server.rooms.ServerRoom;
import com.emptypockets.spacemania.utils.PoolsManager;

/**
 * Created by jenfield on 14/05/2015.
 */
public class ClientRoom implements Disposable{
    int id;
    int maxPlayers;
    String name;

    ClientPlayer host;
    HashSet<ClientPlayer> players;

    public ClientRoom(){
        players = new HashSet<ClientPlayer>();
    }

    public void processMessages(ClientManager manager, ArrayList<ClientRoomMessage> messages){
        for(ClientRoomMessage message : messages){
            message.processMessage(manager,this);
        }
    }

    public void read(ServerRoom room){
        setId(room.getId());
        setMaxPlayers(room.getMaxPlayers());
        setName(room.getName());
        if(room.getHost() != null) {
            setHost(room.getHost().getClientPlayer());
        }
        players.clear();
        room.getPlayerManager().process(new SingleProcessor<ServerPlayer>() {
            @Override
            public void process(ServerPlayer entity) {
                ClientPlayer player = PoolsManager.obtain(ClientPlayer.class);
                player.read(entity);
                players.add(player);
            }
        });
    }

    public ClientPlayer getHost() {
        return host;
    }

    public void setHost(ClientPlayer host) {
        this.host = host;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void update(){
    }

    public void dispose() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlayer(ClientPlayer player) {
        players.add(player);
    }

    public void removePlayer(ClientPlayer player){
        players.remove(player);
    }

    public void logStatus(Console console) {
    	console.println("Room : " + getName());
        console.println("Host : " + (host == null ? "None" : host.getUsername()));
        for(ClientPlayer player : players){
                console.println("Player : "+player);
        }
    }

    public synchronized void chatMessage(Console console, long messageTime, String username, String message) {
        console.println(getName()+" : "+String.format(" %s %s: %s", DateFormat.getTimeInstance().format(messageTime),username==null?"":"["+username+"] ", message));
    }

	public Object getPlayerCount() {
		return players.size();
	}
}
