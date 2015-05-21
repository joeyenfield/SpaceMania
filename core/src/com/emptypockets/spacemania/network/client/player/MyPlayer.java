package com.emptypockets.spacemania.network.client.player;

/**
 * Created by jenfield on 15/05/2015.
 */
public class MyPlayer extends ClientPlayer {

	int entityId = 0;
	
	public void read(MyPlayer player){
		read((ClientPlayer)player);
		entityId = player.getEntityId();
	}
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public int getEntityId() {
		return entityId;
	}
}
