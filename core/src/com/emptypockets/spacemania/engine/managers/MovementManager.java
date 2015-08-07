package com.emptypockets.spacemania.engine.managers;

import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystemManager;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.controls.ControlComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.AngularMovementComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.ConstrainedRegionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ContentModelContainer;

public class MovementManager extends EntitySystemManager implements SingleProcessor<GameEntity> {
	float deltaTime = 0;
	int mask = ComponentType.LINEAR_MOVEMENT.getMask() | ComponentType.ANGULAR_MOVEMENT.getMask() | ComponentType.CONSTRAINED_MOVEMENT.getMask();

	@Override
	public void manage(EntitySystem entitySystem, float deltaTime) {
		this.deltaTime = deltaTime;

		entitySystem.process(this, mask);
	}

	@Override
	public void process(GameEntity entity) {
		ControlComponent controls = entity.getComponent(ComponentType.CONTROL, ControlComponent.class);
		if (controls != null) {
			controls.update(deltaTime);
		}
		LinearMovementComponent linearMovementComponent = entity.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class);
		if (linearMovementComponent != null) {
			linearMovementComponent.update(deltaTime);
		}

		AngularMovementComponent angularMovementComponent = entity.getComponent(ComponentType.ANGULAR_MOVEMENT, AngularMovementComponent.class);
		if (angularMovementComponent != null) {
			angularMovementComponent.update(deltaTime);
		}

		ConstrainedRegionComponent constrainedRegionComponent = entity.getComponent(ComponentType.CONSTRAINED_MOVEMENT, ConstrainedRegionComponent.class);
		if (constrainedRegionComponent != null) {
			constrainedRegionComponent.update(deltaTime);
		}

	}

}
