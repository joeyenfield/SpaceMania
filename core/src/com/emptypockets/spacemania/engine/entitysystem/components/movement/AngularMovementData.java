package com.emptypockets.spacemania.engine.entitysystem.components.movement;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;

public class AngularMovementData extends ComponentData<AngularMovementData> {
	public float angVel = 0;
	public float angAcl = 0;
	boolean lockAngleToLinearVelocity = true;

	@Override
	public void getComponentData(AngularMovementData result) {
		result.angVel = angVel;
		result.angAcl = angAcl;
		result.lockAngleToLinearVelocity = lockAngleToLinearVelocity;
	}

	@Override
	public void setComponentData(AngularMovementData data) {
		angVel = data.angVel;
		angAcl = data.angAcl;
		lockAngleToLinearVelocity = data.lockAngleToLinearVelocity;
	}

	@Override
	public boolean changed(AngularMovementData data) {
		if (Math.abs(angVel - data.angVel) > 0.01) {
			return true;
		}
		if (Math.abs(angAcl - data.angAcl) > 0.01) {
			return true;
		}
		if (lockAngleToLinearVelocity != data.lockAngleToLinearVelocity) {
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		angAcl = 0;
		angVel = 0;
		lockAngleToLinearVelocity = true;
	}

}
