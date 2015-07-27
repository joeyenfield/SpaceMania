package com.emptypockets.spacemania.engine.entitysystem.components.transform;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;

public class LinearTransformData extends ComponentData<LinearTransformData> {
	public Vector2 pos = new Vector2();

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	@Override
	public void getComponentData(LinearTransformData result) {
		result.pos.set(pos);
	}

	@Override
	public void setComponentData(LinearTransformData data) {
		pos.set(data.pos);
	}

	@Override
	public boolean changed(LinearTransformData data) {
		if (pos.dst2(data.pos) > 0.1) {
			return true;
		}
		return false;
	}

}
