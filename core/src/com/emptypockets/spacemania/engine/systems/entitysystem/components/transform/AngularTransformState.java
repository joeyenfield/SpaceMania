package com.emptypockets.spacemania.engine.systems.entitysystem.components.transform;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;

public class AngularTransformState extends ComponentState<AngularTransformState> {
	public float ang = 0;

	public float getAng() {
		return ang;
	}

	public void setAng(float ang) {
		this.ang = ang;
	}

	@Override
	public void readComponentState(AngularTransformState result) {
		result.ang = ang;
	}

	@Override
	public void writeComponentState(AngularTransformState data) {
		ang = data.ang;
	}

	@Override
	public boolean hasStateChanged(AngularTransformState data) {
		if (ang != data.ang) {
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		ang = 0;
	}

}
