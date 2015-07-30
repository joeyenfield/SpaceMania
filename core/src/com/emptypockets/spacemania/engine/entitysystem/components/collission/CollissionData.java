package com.emptypockets.spacemania.engine.entitysystem.components.collission;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;

public class CollissionData extends ComponentData<CollissionData> {
	public float collissionRadius = 1;
	@Override
	public void getComponentData(CollissionData result) {
	}

	@Override
	public void setComponentData(CollissionData data) {
	}

	@Override
	public boolean changed(CollissionData data) {
		return false;
	}

	@Override
	public void reset() {
		collissionRadius = 1;
	}

}
