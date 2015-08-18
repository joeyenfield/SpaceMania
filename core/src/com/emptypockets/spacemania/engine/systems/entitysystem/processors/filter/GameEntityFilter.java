package com.emptypockets.spacemania.engine.systems.entitysystem.processors.filter;

import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;

public interface GameEntityFilter {
	public boolean accept(GameEntity entity);
}
