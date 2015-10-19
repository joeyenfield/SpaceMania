package com.emptypockets.spacemania.engine.systems.entitysystem.components.movement;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class AngularMovementComponent extends EntityComponent<AngularMovementState> {
	static float MIN_DELTA = 0.01f;

	public AngularMovementComponent() {
		super(ComponentType.ANGULAR_MOVEMENT);
	}

	@Override
	public void setupState() {
		super.setupState();
		networkSync = true;
	}

	public void update(float deltaTime) {
		if (state == null) {
			return;
		}
		if (state.lockAngleToLinearVelocity) {
			LinearMovementComponent linear = (LinearMovementComponent) entity.getComponent(ComponentType.LINEAR_MOVEMENT);
			if (linear != null) {
				if (linear.state.vel.len2() > 0.01) {
					entity.angularTransform.state.ang = linear.state.vel.angle();
					return;
				}
			}
		}

		state.angVel += state.angAcl * deltaTime;
		entity.angularTransform.state.ang += state.angVel * deltaTime;
	}

	@Override
	public Class<AngularMovementState> getStateClass() {
		return AngularMovementState.class;
	}
}
