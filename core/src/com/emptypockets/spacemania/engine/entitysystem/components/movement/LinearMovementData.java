package com.emptypockets.spacemania.engine.entitysystem.components.movement;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;

public class LinearMovementData extends ComponentData<LinearMovementData> {
	public Vector2 vel = new Vector2();
	public Vector2 acl = new Vector2();

	@Override
	public void getComponentData(LinearMovementData result) {
		result.vel.set(vel);
		result.acl.set(acl);
	}

	@Override
	public void setComponentData(LinearMovementData data) {
		vel.set(data.vel);
		acl.set(data.acl);
	}

	@Override
	public boolean changed(LinearMovementData data) {
		if (vel.dst2(data.vel) > 0.01) {
			return true;
		}

		if (acl.dst2(data.acl) > 0.01) {
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		vel.setZero();
		acl.setZero();
	}

}
