package com.emptypockets.spacemania.engine.dynamics;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class ConstrainedMovementProcessor implements SingleProcessor<GameEntity>{
	float deltaTime;
	@Override
	public void process(GameEntity entity) {
		entity.getComponent(ComponentType.CONSTRAINED_MOVEMENT).update(deltaTime);
	}

}
