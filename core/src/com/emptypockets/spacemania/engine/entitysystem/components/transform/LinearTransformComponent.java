package com.emptypockets.spacemania.engine.entitysystem.components.transform;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

public class LinearTransformComponent extends EntityComponent<LinearTransformData> {

	public LinearTransformComponent(GameEntity object) {
		super(object, ComponentType.LINEAR_TRANSFORM, new LinearTransformData());
	}

	@Override
	public void update(float deltaTime) {
	}

}
