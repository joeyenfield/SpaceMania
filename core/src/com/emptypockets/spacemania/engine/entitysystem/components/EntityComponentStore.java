package com.emptypockets.spacemania.engine.entitysystem.components;

import com.emptypockets.spacemania.utils.PoolsManager;

public class EntityComponentStore {

	public EntityComponent[] component = ComponentType.getComponentHolder();

	public <TYPE extends EntityComponent> TYPE get(ComponentType type, Class<TYPE> classType) {
		return (TYPE) component[type.id];
	}

	public EntityComponent get(ComponentType type) {
		return component[type.id];
	}

	public void add(EntityComponent compData) {
		component[compData.componentType.id] = compData;
	}

	public void remove(ComponentType type) {
		PoolsManager.free(component[type.id]);
		component[type.id] = null;
	}

	public void clear() {
		for (int i = 0; i < component.length; i++) {
			if (component[i] != null) {
				PoolsManager.free(component[i]);
				component[i] = null;
			}
		}
	}
}
