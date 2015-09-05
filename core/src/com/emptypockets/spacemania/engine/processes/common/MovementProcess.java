package com.emptypockets.spacemania.engine.processes.common;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystemManager;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.controls.ControlComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.AngularMovementComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.ConstrainedRegionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class MovementProcess extends EntitySystemManager implements SingleProcessor<GameEntity>, EngineProcess<GameEngine> {
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

	@Override
	public void process(GameEngine engine) {
		manage(engine.entitySystem, engine.getDeltaTime());
	}

	@Override
	public void preProcess(GameEngine engine) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postProcess(GameEngine engine) {
		// TODO Auto-generated method stub

	}
}
