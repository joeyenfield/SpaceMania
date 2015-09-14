package com.emptypockets.spacemania.engine.systems.entitysystem.components.controls;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentData;

public class ControlData extends ComponentData<ControlData> {
	public Vector2 move = new Vector2();
	public Vector2 shootDir = new Vector2();
	public boolean shooting;

	@Override
	public void getComponentData(ControlData result) {
		result.move.set(move);
		result.shooting = shooting;
	}

	@Override
	public void setComponentData(ControlData data) {
		this.move.set(data.move);
		this.shooting = data.shooting;
	}

	@Override
	public boolean changed(ControlData data) {
		// Never set shoot so clients dont shoot
		if (shooting != data.shooting) {
			return true;
		}
		if (this.move.dst2(data.move) > 0.01) {
			return true;
		}
		if(this.shootDir.dst2(data.shootDir) > 0.01){
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		move.setZero();
		shootDir.setZero();
		shooting = false;
	}
	
}
