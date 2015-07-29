package com.emptypockets.spacemania.engine.managers;

import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystemManager;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class PartitionManager extends EntitySystemManager implements SingleProcessor<GameEntity>{

	float deltaTime = 0;
	@Override
	public void manage(EntitySystem entitySystem, float deltaTime) {
		this.deltaTime = deltaTime;
		entitySystem.process(this, ComponentType.PARTITION);
	}

	@Override
	public void process(GameEntity entity) {
		entity.getComponent(ComponentType.PARTITION).update(deltaTime);
	}

}
