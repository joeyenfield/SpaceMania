package com.emptypockets.spacemania.engine.entitysystem.components.controls;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;

public class ControlData extends ComponentData<ControlData> {
	Vector2 move = new Vector2();
	Vector2 shoot = new Vector2();

	@Override
	public void getComponentData(ControlData result) {
	}

	@Override
	public void setComponentData(ControlData data) {
	}

	@Override
	public boolean changed(ControlData data) {
		return false;
	}

	@Override
	public void reset() {
		move.setZero();
		shoot.setZero();
	}

}
