package com.emptypockets.spacemania.engine.systems.entitysystem.processors.filter;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;

public class GameEntityTypeFilter implements GameEntityFilter, Poolable {
	public GameEntityType type;

	@Override
	public boolean accept(GameEntity entity) {
		return entity.type.equals(type);
	}

	@Override
	public void reset() {
		type = null;
	}

}