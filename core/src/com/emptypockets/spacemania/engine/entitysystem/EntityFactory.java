package com.emptypockets.spacemania.engine.entitysystem;

import com.emptypockets.spacemania.engine.entitysystem.components.movement.AngularMovementComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementComponent;

public class EntityFactory {
	public GameEntity createEntity(int entityId){
		GameEntity entity = new GameEntity(entityId);
		entity.addComponent(new LinearMovementComponent(entity));
		entity.addComponent(new AngularMovementComponent(entity));
		return entity;
	}
}
