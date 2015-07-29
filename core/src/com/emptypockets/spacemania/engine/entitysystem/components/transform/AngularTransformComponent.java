package com.emptypockets.spacemania.engine.entitysystem.components.transform;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

public class AngularTransformComponent extends EntityComponent<AngularTransformData> {
	public AngularTransformComponent() {
		super(ComponentType.ANGULAR_TRANSFORM, new AngularTransformData());
	}

	@Override
	public void update(float deltaTime) {
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
