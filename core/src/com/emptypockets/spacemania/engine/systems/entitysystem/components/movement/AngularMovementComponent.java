package com.emptypockets.spacemania.engine.systems.entitysystem.components.movement;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class AngularMovementComponent extends EntityComponent<AngularMovementData> {
	static float MIN_DELTA = 0.01f;

	public AngularMovementComponent() {
		super(ComponentType.ANGULAR_MOVEMENT);
	}

	@Override
	public void setupData() {
		super.setupData();
		networkSync = true;
	}

	public void update(float deltaTime) {
		if (data == null) {
			return;
		}
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
	public Class<AngularMovementData> getDataClass() {
		return AngularMovementData.class;
	}
}
