package com.emptypockets.spacemania.network.client.payloads;

import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;

public class ClientMyPlayerStateUpdatePayload extends ClientPayload {
	ClientPlayer myPlayer;

	public ClientPlayer getMyPlayer() {
		return myPlayer;
	}

	public void setMyPlayer(ClientPlayer myPlayer) {
		this.myPlayer = myPlayer;
	}

	@Override
	public void executePayload(ClientManager clientManager) {
		ClientPlayer player = clientManager.getPlayer();
		if(player != null){
			player.read(myPlayer);
		}
	}
}
