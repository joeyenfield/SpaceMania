package com.emptypockets.spacemania.engine.processes.client;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngineClient;
import com.emptypockets.spacemania.engine.network.client.ClientPlayerAdapter;

public class ClientNetworkProcess implements EngineProcess<GameEngineClient> {

	public ArrayList<ClientPlayerAdapter> adapters = new ArrayList<ClientPlayerAdapter>();

	public synchronized void processOutgoingData(GameEngineClient gameEngine) {
		int size = adapters.size();
		for (int i = 0; i < size; i++) {
			ClientPlayerAdapter adapter = adapters.get(i);
			adapter.processOutgoing(gameEngine);
		}
	}

	public synchronized void processIncommingData(GameEngineClient gameEngine) {
		int size = adapters.size();
		for (int i = 0; i < size; i++) {
			ClientPlayerAdapter adapter = adapters.get(i);
			adapter.processIncomming(gameEngine);
		}
	}

	public synchronized void update(GameEngineClient gameEngine) {
		int size = adapters.size();
		for (int i = 0; i < size; i++) {
			ClientPlayerAdapter adapter = adapters.get(i);
			adapter.update(gameEngine);
		}
	}

	@Override
	public void process(GameEngineClient engine) {
		update(engine);
	}

	@Override
	public void preProcess(GameEngineClient engine) {
		processIncommingData(engine);
	}

	@Override
	public void postProcess(GameEngineClient engine) {
		processOutgoingData(engine);
	}
}
