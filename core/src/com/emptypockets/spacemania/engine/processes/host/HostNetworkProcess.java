package com.emptypockets.spacemania.engine.processes.host;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.GameEngineHost;
import com.emptypockets.spacemania.engine.network.host.HostPlayerAdapter;
import com.emptypockets.spacemania.metrics.plotter.DataLogger;

public class HostNetworkProcess implements EngineProcess<GameEngineHost> {

	public ArrayList<HostPlayerAdapter> connections;

	long lastProcessTime = 0;
	long desiredProcessTime = 100;

	public void processOutgoingData(GameEngineHost gameEngine) {
		if (connections != null) {
			if (System.currentTimeMillis() - lastProcessTime > desiredProcessTime) {
				if (DataLogger.isEnabled()) {
					DataLogger.log(gameEngine.getName() + "-net", 1);
				}
				lastProcessTime = System.currentTimeMillis();
				synchronized (connections) {
					int size = connections.size();
					for (int i = 0; i < size; i++) {
						HostPlayerAdapter connection = connections.get(i);
						connection.processOutgoingPackets(gameEngine);
					}
				}
			}
		}
	}

	public void processIncommingData(GameEngine gameEngine) {

	}

	@Override
	public void process(GameEngineHost engine) {
		synchronized (connections) {
			int size = connections.size();
			for (int i = 0; i < size; i++) {
				HostPlayerAdapter connection = connections.get(i);
				connection.update(engine);
			}
		}
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
