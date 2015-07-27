package com.emptypockets.spacemania.engine.entitysystem.components.transform;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

public class AngularTransformComponent extends EntityComponent<AngularTransformData> {
	public AngularTransformComponent(GameEntity object) {
		super(object, ComponentType.ANGULAR_TRANSFORM, new AngularTransformData());
	}

	@Override
	public void update(float deltaTime) {
	}

}
