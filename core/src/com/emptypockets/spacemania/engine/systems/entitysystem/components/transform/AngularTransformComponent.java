package com.emptypockets.spacemania.engine.systems.entitysystem.components.transform;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class AngularTransformComponent extends EntityComponent<AngularTransformState> {
	public AngularTransformComponent() {
		super(ComponentType.ANGULAR_TRANSFORM);
	}

	public void update(float deltaTime) {
	}

	@Override
	public Class<AngularTransformState> getStateClass() {
		return AngularTransformState.class;
	}

}
