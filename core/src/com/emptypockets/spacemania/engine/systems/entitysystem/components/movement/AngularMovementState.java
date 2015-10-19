package com.emptypockets.spacemania.engine.systems.entitysystem.components.movement;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;

public class AngularMovementState extends ComponentState<AngularMovementState> {
	public float angVel = 0;
	public float angAcl = 0;
	boolean lockAngleToLinearVelocity = true;

	@Override
	public void readComponentState(AngularMovementState result) {
		result.angVel = angVel;
		result.angAcl = angAcl;
		result.lockAngleToLinearVelocity = lockAngleToLinearVelocity;
	}

	@Override
	public void writeComponentState(AngularMovementState data) {
		angVel = data.angVel;
		angAcl = data.angAcl;
		lockAngleToLinearVelocity = data.lockAngleToLinearVelocity;
	}

	@Override
	public boolean hasStateChanged(AngularMovementState data) {
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
