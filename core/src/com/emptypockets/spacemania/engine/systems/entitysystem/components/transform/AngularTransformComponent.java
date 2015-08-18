package com.emptypockets.spacemania.engine.systems.entitysystem.components.transform;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class AngularTransformComponent extends EntityComponent<AngularTransformData> {
	public AngularTransformComponent() {
		super(ComponentType.ANGULAR_TRANSFORM);
	}

	public void update(float deltaTime) {
	}

	@Override
	public Class<AngularTransformData> getDataClass() {
		return AngularTransformData.class;
	}

}
