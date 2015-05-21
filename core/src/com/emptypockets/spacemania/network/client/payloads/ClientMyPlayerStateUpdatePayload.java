package com.emptypockets.spacemania.network.client.payloads;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.client.player.MyPlayer;

public class ClientMyPlayerStateUpdatePayload extends ClientPayload {
	MyPlayer myPlayer;

	public MyPlayer getMyPlayer() {
		return myPlayer;
	}

	public void setMyPlayer(MyPlayer myPlayer) {
		this.myPlayer = myPlayer;
	}

	@Override
	public void executePayload(ClientManager clientManager) {
		MyPlayer player = clientManager.getPlayer();
		if(player != null){
			player.read(myPlayer);
		}
	}
}
