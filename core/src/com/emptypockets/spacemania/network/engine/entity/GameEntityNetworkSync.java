package com.emptypockets.spacemania.network.engine.entity;

import java.util.HashMap;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;

public class GameEntityNetworkSync implements Poolable {
	public int entityId;
	public HashMap<ComponentType, ComponentState> data = new HashMap<ComponentType, ComponentState>();

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
