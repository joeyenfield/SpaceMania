package com.emptypockets.spacemania.engine.systems.entitysystem.components.collission;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;

public class CollissionState extends ComponentState<CollissionState> {
	public float collissionRadius = 1;
	@Override
	public void readComponentState(CollissionState result) {
	}

	@Override
	public void writeComponentState(CollissionState data) {
	}

	@Override
	public boolean hasStateChanged(CollissionState data) {
		return false;
	}

	@Override
	public void reset() {
		collissionRadius = 1;
	}

}
