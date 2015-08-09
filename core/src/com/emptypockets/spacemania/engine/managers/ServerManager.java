package com.emptypockets.spacemania.engine.managers;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.engines.GameEngine;
import com.emptypockets.spacemania.network.ClientEngineSyncManager;

public class ServerManager {

	public ArrayList<ClientEngineSyncManager> connections;

	long lastProcessTime = 0;
	long desiredProcessTime = 100;

	public void processOutgoingData(GameEngine gameEngine) {
		if (connections != null) {
			if (System.currentTimeMillis() - lastProcessTime > desiredProcessTime) {
				lastProcessTime = System.currentTimeMillis();
				synchronized (connections) {
					int size = connections.size();
					for (int i = 0; i < size; i++) {
						ClientEngineSyncManager connection = connections.get(i);
						connection.update(gameEngine);
					}
				}
			}
		}
	}

	public void processIncommingData(GameEngine gameEngine) {

	}
}
