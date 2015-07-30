package com.emptypockets.spacemania.engine.entitysystem.components;

import com.emptypockets.spacemania.utils.PoolsManager;

public class EntityComponentStore {

	EntityComponent[] data = ComponentType.getComponentHolder();

	public <TYPE extends EntityComponent> TYPE get(ComponentType type, Class<TYPE> classType) {
		return (TYPE) data[type.id];
	}

	public EntityComponent get(ComponentType type) {
		return data[type.id];
	}

	public void add(EntityComponent compData) {
		data[compData.componentType.id] = compData;
	}

	public void remove(ComponentType type) {
		data[type.id] = null;
	}

	public void clear() {
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				PoolsManager.free(data[i]);
				data[i] = null;
			}
		}
	}
}
