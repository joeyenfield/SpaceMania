package com.emptypockets.spacemania.engine.systems.entitysystem.components.transform;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class LinearTransformComponent extends EntityComponent<LinearTransformState> {
	public LinearTransformComponent() {
		super(ComponentType.LINEAR_TRANSFORM);
	}

	@Override
	public void setupState() {
		super.setupState();
		networkSync = true;
	}

	public void update(float deltaTime) {
	}

	@Override
	public Class<LinearTransformState> getStateClass() {
		return LinearTransformState.class;
	}
}
