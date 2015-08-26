package com.emptypockets.spacemania.engine.systems.entitysystem.components.transform;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class ClientLinearTransformComponent extends LinearTransformComponent {
	float errorTolerance2 = 1000*1000;
	float fixFactor = 0.05f;
	Vector2 offset = new Vector2();
	boolean first = true;
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		float dX = 0;
		float dY = 0;
		if (offset.len2() > 1) {
			dX = offset.x * fixFactor;
			dY = offset.y * fixFactor;
			offset.x -= dX;
			offset.y -= dY;
			data.pos.add(dX, dY);
		}
	}

	@Override
	public void writeData(LinearTransformData data) {
		if (this.data.pos.dst2(data.pos) > errorTolerance2|| first) {
			super.writeData(data);
			offset.setZero();
			first=false;
		} else {
			offset.set(this.data.pos).sub(data.pos);
		}
	}

	@Override
	public void reset() {
		super.reset();
		offset.setZero();
		first = true;
	}
}
