package com.emptypockets.spacemania.engine.dynamics;

import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystemManager;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;

public class MovementProcessor extends EntitySystemManager {
	LinearMovementProcessor linearMovementProcessor = new LinearMovementProcessor();
	AngularMovementProcessor angularMovementProcessor = new AngularMovementProcessor();
	ConstrainedMovementProcessor constrainedMovementProcessor = new ConstrainedMovementProcessor();

	@Override
	public void manage(EntitySystem entitySystem, float deltaTime) {
		linearMovementProcessor.deltaTime = deltaTime;
		entitySystem.process(linearMovementProcessor, ComponentType.LINEAR_MOVEMENT.getMask());
		angularMovementProcessor.deltaTime = deltaTime;
		entitySystem.process(angularMovementProcessor, ComponentType.ANGULAR_MOVEMENT.getMask());
		constrainedMovementProcessor.deltaTime = deltaTime;
		entitySystem.process(constrainedMovementProcessor, ComponentType.CONSTRAINED_MOVEMENT.getMask());
	}

}
