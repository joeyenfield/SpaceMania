package com.emptypockets.spacemania.engine.processes.client;

import com.badlogic.gdx.utils.Bits;
import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngineClient;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class NetworkLinearTransformFixer implements SingleProcessor<GameEntity>, EngineProcess<GameEngineClient> {
	float deltaTime = 0;
	Bits mask = ComponentType.LINEAR_TRANSFORM.getMask();

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
