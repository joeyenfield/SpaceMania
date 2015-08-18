package com.emptypockets.spacemania.engine.systems.entitysystem.components.transform;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class LinearTransformComponent extends EntityComponent<LinearTransformData> {

	public LinearTransformComponent() {
		super(ComponentType.LINEAR_TRANSFORM);
		networkSync = true;
	}

	public void update(float deltaTime) {
	}

	@Override
	public Class<LinearTransformData> getDataClass() {
		return LinearTransformData.class;
	}
}
