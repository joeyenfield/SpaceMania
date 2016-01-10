package com.emptypockets.spacemania.network.engine.entity;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;

public class GameEntityAdded implements Poolable {
	public int id;
	public GameEntityType type;

	@Override
	public void reset() {
		type = null;
		id = 0;
	}
}
