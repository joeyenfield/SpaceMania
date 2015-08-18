package com.emptypockets.spacemania.network.common.data.engine.entity;

import java.util.HashMap;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentData;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;

public class GameEntityNetworkSync implements Poolable {
	public int entityId;
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
