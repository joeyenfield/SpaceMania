package com.emptypockets.spacemania.network.transport.data.engine.entity;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.entitysystem.GameEntityType;

public class GameEntityAdded implements Poolable {
	public long id;
	public GameEntityType type;

	@Override
	public void reset() {
		type = null;
		id = 0;
	}
}
