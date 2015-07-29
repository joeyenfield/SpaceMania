package com.emptypockets.spacemania.engine.entitysystem.components.transform;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;

public class AngularTransformData extends ComponentData<AngularTransformData> {
	public float ang = 0;

	public float getAng() {
		return ang;
	}

	public void setAng(float ang) {
		this.ang = ang;
	}

	@Override
	public void getComponentData(AngularTransformData result) {
		result.ang = ang;
	}

	@Override
	public void setComponentData(AngularTransformData data) {
		ang = data.ang;
	}

	@Override
	public boolean changed(AngularTransformData data) {
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
