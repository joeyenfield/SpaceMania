package com.emptypockets.spacemania.network.client;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.GameEngineClient;
import com.emptypockets.spacemania.network.common.data.engine.GameEngineState;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ClientPlayerAdapter {

	ClientDataSyncManager clientDataSyncManager = new ClientDataSyncManager();
	ArrayList<GameEngineState> states = new ArrayList<GameEngineState>();

	public synchronized void processIncomming(GameEngineClient clientEngine) {
		int size = states.size();
		
		for(int i = 0; i < size; i++){
			System.out.println("PROCESSING");
			GameEngineState lastState = states.get(i);
			clientDataSyncManager.apply(clientEngine, lastState);
			PoolsManager.free(lastState);
		}
	}

	public synchronized void recieve(GameEngineState state) {
		states.add(state);
	}

}
