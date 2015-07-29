package com.emptypockets.spacemania.engine.entitysystem.components.movement;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

public class AngularMovementComponent extends EntityComponent<AngularMovementData> {
	static float MIN_DELTA = 0.01f;

	public AngularMovementComponent() {
		super(ComponentType.ANGULAR_MOVEMENT, new AngularMovementData());
	}

	public void update(float deltaTime) {
		if (data.lockAngleToLinearVelocity) {
			LinearMovementComponent linear = (LinearMovementComponent) entity.getComponent(ComponentType.LINEAR_MOVEMENT);
			if (linear != null) {
				if (linear.data.vel.len2() > 0.01) {
					entity.angularTransform.data.ang = linear.data.vel.angle();
					return;
				}
			}
		}

		data.angVel += data.angAcl * deltaTime;
		entity.angularTransform.data.ang += data.angVel * deltaTime;
	}

	@Override
	public void reset() {
		
	}

}
