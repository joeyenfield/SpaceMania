package com.emptypockets.spacemania.network.transport.data.engine.entity;

import java.util.HashMap;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;

public class GameEntityNetworkSync implements Poolable {
	long entityId;
	public HashMap<ComponentType, ComponentData> data = new HashMap<ComponentType, ComponentData>();

	public void clear() {
		data.clear();
	}

	public void printId() {
		for (ComponentType type : data.keySet()) {
			System.out.println(type + " - " + data.get(type));
		}
	}

	@Override
	public void reset() {
		clear();
	}

}
