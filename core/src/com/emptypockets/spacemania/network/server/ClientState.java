package com.emptypockets.spacemania.network.server;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.transport.ClientStateTransferObject;

public class ClientState {
	String username;
	Vector2 pos;
	Vector2 vel;
	long lastClientTime;
	
	public ClientState(String username){
		this.username = username;
		pos = new Vector2();
		vel = new Vector2();
		lastClientTime = 0;
	}
	public void clientDataRecieved(ClientStateTransferObject data){
		if(lastClientTime < data.time){
			vel.x = data.valueX;
			vel.y = data.valueY;
			lastClientTime = data.time;
		}
	}
}
