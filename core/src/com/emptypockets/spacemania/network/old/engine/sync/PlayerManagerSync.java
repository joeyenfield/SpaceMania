package com.emptypockets.spacemania.network.old.engine.sync;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.holders.CleanerProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.old.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.old.client.player.ClientPlayerManager;
import com.emptypockets.spacemania.network.old.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.old.server.player.ServerPlayerManager;
import com.emptypockets.spacemania.utils.PoolsManager;

public class PlayerManagerSync implements Poolable {
	ArrayList<ClientPlayer> players = new ArrayList<ClientPlayer>();

	public void read(ServerPlayerManager serverPlayers) {
		players.clear();
		serverPlayers.process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer entity) {
				players.add(entity.getClientPlayer());
			}
		});
	}

	public void write(ClientPlayerManager clientPlayers) {
		// Remove missing players
		clientPlayers.process(new CleanerProcessor<ClientPlayer>() {
			@Override
			public boolean shouldRemove(ClientPlayer entity) {
				int size = players.size();
				for (int i = 0; i < size; i++) {
					ClientPlayer serverData = players.get(i);
					if(serverData.getId() == entity.getId()){
						return false;
					}
				}
				return true;
			}
		});
		
		//Update or add new
		int size = players.size();
		for (int i = 0; i < size; i++) {
			ClientPlayer serverData = players.get(i);
			ClientPlayer clientData = clientPlayers.getPlayerById(serverData.getId());
			if (clientData == null) {
				clientData = PoolsManager.obtain(ClientPlayer.class);
				clientData.read(serverData);
				clientPlayers.addPlayer(clientData);
			} else {
				clientData.read(serverData);
			}
		}
		releaseDataToPool();
	}

	public void releaseDataToPool() {
		int size = players.size();
		for (int i = 0; i < size; i++) {
			PoolsManager.free(players.get(i));
		}
	}

	@Override
	public void reset() {
		players.clear();
	}
}
