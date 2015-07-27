package com.emptypockets.spacemania.engine.entitysystem.components.movement;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

public class LinearMovementComponent extends EntityComponent<LinearMovementData> {
	static float MIN_DELTA = 0.01f;

	public LinearMovementComponent(GameEntity entity) {
		super(entity, ComponentType.LINEAR_MOVEMENT, new LinearMovementData());
	}

	public void update(float deltaTime) {
		if (data.acl.len2() > MIN_DELTA) {
			data.vel.x += data.acl.x * deltaTime;
			data.vel.y += data.acl.y * deltaTime;
		} else {
			data.acl.x = 0;
			data.acl.y = 0;
		}

		if (data.vel.len2() > MIN_DELTA) {
			entity.linearTransform.data.pos.x += entity.linearTransform.data.pos.x * deltaTime;
			entity.linearTransform.data.pos.y += entity.linearTransform.data.pos.y * deltaTime;
		} else {
			data.vel.x = 0;
			data.vel.y = 0;
		}
	}

}
