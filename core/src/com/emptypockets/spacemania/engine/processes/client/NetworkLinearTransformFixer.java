package com.emptypockets.spacemania.engine.processes.client;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.GameEngineClient;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.controls.ControlComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.AngularMovementComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.ConstrainedRegionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class NetworkLinearTransformFixer implements SingleProcessor<GameEntity>, EngineProcess<GameEngineClient> {
	float deltaTime = 0;
	int mask = ComponentType.LINEAR_TRANSFORM.getMask();

	public void manage(EntitySystem entitySystem, float deltaTime) {
		this.deltaTime = deltaTime;
		entitySystem.process(this, mask);
	}

	@Override
	public void process(GameEntity entity) {
		entity.linearTransform.update(deltaTime);
	}

	@Override
	public void process(GameEngineClient engine) {
		manage(engine.entitySystem, engine.getDeltaTime());
	}

	@Override
	public void preProcess(GameEngineClient engine) {
	}

	@Override
	public void postProcess(GameEngineClient engine) {
	}

}
