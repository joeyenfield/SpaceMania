package com.emptypockets.spacemania.network.old.client.player;

import com.emptypockets.spacemania.network.old.server.player.ServerPlayer;

/**
 * Created by jenfield on 14/05/2015.
 */
public class ClientPlayer{

    int id;
    int ping;
    String username;
    int entityId;
    int deaths;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPing() {
        return ping;
    }
    public void setPing(int ping) {
        this.ping = ping;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void read(ServerPlayer player) {
        id = player.getId();
        username = player.getUsername();
        ping = player.getPing();
        entityId = player.getEntityId();
    }
    
    public void read(ClientPlayer player){
    	id = player.getId();
        username = player.getUsername();
        ping = player.getPing();
        entityId = player.getEntityId();
    }

    public void dispose() {
        username = null;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if(o instanceof ClientPlayer){
            if(id == ((ClientPlayer) o).getId()){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Username[");
        result.append(username);
        result.append("] Ping [");
        result.append(ping);
        result.append("]");
        return result.toString();
    }
	public int getEntityId() {
		return entityId;
	}
	public int getDeaths() {
		return deaths;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
}