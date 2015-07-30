package com.emptypockets.spacemania.engine.entitysystem.components.controls;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementComponent;

public class ControlComponent extends EntityComponent<ControlData> {

	public ControlComponent() {
		super(ComponentType.CONTROL);
	}

	public void update(float deltaTime) {
		entity.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).data.vel.x = data.move.x;
		entity.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).data.vel.y = data.move.y;
	}

	@Override
	public Class<ControlData> getDataClass() {
		return ControlData.class;
	}

}
