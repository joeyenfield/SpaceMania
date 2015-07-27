package com.emptypockets.spacemania.engine.entitysystem.components.movement;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

public class AngularMovementComponent extends EntityComponent<AngularMovementData> {
	static float MIN_DELTA = 0.01f;

	public AngularMovementComponent(GameEntity entity) {
		super(entity, ComponentType.ANGULAR_MOVEMENT, new AngularMovementData());
	}

	public void update(float deltaTime) {
		data.angVel += data.angAcl * deltaTime;
		entity.angularTransform.data.ang += data.angVel * deltaTime;
	}

}
