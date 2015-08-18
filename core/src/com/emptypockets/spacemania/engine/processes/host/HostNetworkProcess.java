package com.emptypockets.spacemania.engine.processes.host;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.GameEngineHost;
import com.emptypockets.spacemania.network.host.HostPlayerAdapter;

public class HostNetworkProcess implements EngineProcess<GameEngineHost> {

	public ArrayList<HostPlayerAdapter> connections;

	long lastProcessTime = 0;
	long desiredProcessTime = 5000;

	public void processOutgoingData(GameEngineHost gameEngine) {
		if (connections != null) {
			if (System.currentTimeMillis() - lastProcessTime > desiredProcessTime) {
				lastProcessTime = System.currentTimeMillis();
				synchronized (connections) {
					System.out.println("Broadcast");
					int size = connections.size();
					for (int i = 0; i < size; i++) {
						HostPlayerAdapter connection = connections.get(i);
						connection.update(gameEngine);
					}
				}
			}
		}
	}

	public void processIncommingData(GameEngine gameEngine) {

	}
	
	@Override
	public void process(GameEngineHost engine) {
	}

	@Override
	public void preProcess(GameEngineHost engine) {
		processIncommingData(engine);
	}

	@Override
	public void postProcess(GameEngineHost engine) {
		processOutgoingData(engine);
	}
}
