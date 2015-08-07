package com.emptypockets.spacemania.engine.entitysystem.processors.filter;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;

public interface GameEntityFilter {
	public boolean accept(GameEntity entity);
}
