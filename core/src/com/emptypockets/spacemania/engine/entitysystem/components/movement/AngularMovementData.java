package com.emptypockets.spacemania.engine.entitysystem.components.movement;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;

public class AngularMovementData extends ComponentData<AngularMovementData> {
	public float angVel = 0;
	public float angAcl = 0;

	@Override
	public void getComponentData(AngularMovementData result) {
		result.angVel = angVel;
		result.angAcl = angAcl;
	}

	@Override
	public void setComponentData(AngularMovementData data) {
		angVel = data.angVel;
		angAcl = data.angAcl;
	}

	@Override
	public boolean changed(AngularMovementData data) {
		if (Math.abs(angVel - data.angVel) > 0.01) {
			return true;
		}
		if (Math.abs(angAcl - data.angAcl) > 0.01) {
			return true;
		}
		return false;
	}

}
