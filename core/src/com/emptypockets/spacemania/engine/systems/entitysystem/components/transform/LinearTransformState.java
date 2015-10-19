package com.emptypockets.spacemania.engine.systems.entitysystem.components.transform;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;

public class LinearTransformState extends ComponentState<LinearTransformState> {
	public Vector2 pos = new Vector2();

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	@Override
	public void readComponentState(LinearTransformState result) {
		result.pos.set(pos);
	}

	@Override
	public void writeComponentState(LinearTransformState data) {
		pos.set(data.pos);
	}

	@Override
	public boolean hasStateChanged(LinearTransformState data) {
		if (pos.dst2(data.pos) > 0.1) {
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		pos.setZero();
	}

}
