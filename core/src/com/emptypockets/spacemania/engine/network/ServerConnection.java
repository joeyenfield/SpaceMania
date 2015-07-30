package com.emptypockets.spacemania.engine.network;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.network.NetworkServerData;

public class ServerConnection {
	GameEngine engine;

	ArrayList<GameEntity> tempEntities = new ArrayList<GameEntity>();

	ArrayList<GameEntity> currentEntities = new ArrayList<GameEntity>();
	ArrayList<GameEntity> addedEntities = new ArrayList<GameEntity>();
	ArrayList<GameEntity> removedEntities = new ArrayList<GameEntity>();

	HashMap<GameEntity, NetworkServerData> data;
	
	Rectangle region = new Rectangle();

	public void updateEntities() {
		engine.spatialPartition.searchAnyMask(region, ComponentType.NETWORK_SERVER.getMask(), tempEntities);

		int size = 0;

		size = tempEntities.size();
		// Work out whats been added;
		for (int i = 0; i < size; i++) {
			GameEntity ent = tempEntities.get(i);
			if (!currentEntities.contains(ent)) {
				addedEntities.add(ent);
			}
		}

		// Work out whats been removed
		size = currentEntities.size();
		for (int i = 0; i < size; i++) {
			GameEntity ent = currentEntities.get(i);
			if (!tempEntities.contains(ent)) {
				removedEntities.add(ent);
			}
		}

		ArrayList<GameEntity> tmp = currentEntities;
		currentEntities = tempEntities;
		tempEntities = tmp;
		tempEntities.clear();
	}

	public void addEntity(){
		
	}
}
