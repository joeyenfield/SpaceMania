package com.emptypockets.spacemania.network.common.data.engine.entity;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentData;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ComponentDataStore implements Poolable {

	public ComponentData data[] = ComponentType.getComponentDataHolder();

	@Override
	public void reset() {
		for (int i = 0; i < data.length; i++) {
			if (data[i] != null) {
				PoolsManager.free(data[i]);
				data[i] = null;
			}
		}
	}
}
