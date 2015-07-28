package com.emptypockets.spacemania.engine.dynamics;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class AngularMovementProcessor implements SingleProcessor<GameEntity>{
	float deltaTime;
	@Override
	public void process(GameEntity entity) {
		entity.getComponent(ComponentType.ANGULAR_MOVEMENT).update(deltaTime);
	}

}
