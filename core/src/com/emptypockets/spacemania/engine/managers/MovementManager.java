package com.emptypockets.spacemania.engine.managers;

import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystemManager;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementData;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class MovementManager extends EntitySystemManager implements SingleProcessor<GameEntity> {
	float deltaTime = 0;
	int mask = ComponentType.LINEAR_MOVEMENT.getMask() | ComponentType.ANGULAR_MOVEMENT.getMask() | ComponentType.CONSTRAINED_MOVEMENT.getMask();

	@Override
	public void manage(EntitySystem entitySystem, float deltaTime) {
		this.deltaTime = deltaTime;
		entitySystem.process(this, mask);
	}

	@Override
	public void process(GameEntity entity) {
		EntityComponent<?> comp = null;
		
		comp = entity.getComponent(ComponentType.LINEAR_MOVEMENT);
		if(comp != null){
			comp.update(deltaTime);
		}
		
		comp = entity.getComponent(ComponentType.ANGULAR_MOVEMENT);
		if(comp != null){
			comp.update(deltaTime);
		}
		
		comp = entity.getComponent(ComponentType.CONSTRAINED_MOVEMENT);
		if(comp != null){
			comp.update(deltaTime);
		}
	}

}
