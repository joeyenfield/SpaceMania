package com.emptypockets.spacemania.engine.systems.entitysystem.components.movement;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class LinearMovementComponent extends EntityComponent<LinearMovementData> {
	static float MIN_DELTA = 0.01f;

	public LinearMovementComponent() {
		super(ComponentType.LINEAR_MOVEMENT);
		networkSync = true;
	}

	public void update(float deltaTime) {
		if (data == null) {
			return;
		}
		if (data.acl.len2() > MIN_DELTA) {
			data.vel.x += data.acl.x * deltaTime;
			data.vel.y += data.acl.y * deltaTime;
		} else {
			data.acl.x = 0;
			data.acl.y = 0;
		}

		if (data.vel.len2() > MIN_DELTA) {
			entity.linearTransform.data.pos.x += data.vel.x * deltaTime;
			entity.linearTransform.data.pos.y += data.vel.y * deltaTime;
		} else {
			data.vel.x = 0;
			data.vel.y = 0;
		}
	}

	@Override
	public Class<LinearMovementData> getDataClass() {
		return LinearMovementData.class;
	}
}
