package com.emptypockets.spacemania.engine.processes.common;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.partition.PartitionComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class PartitionProcess implements SingleProcessor<GameEntity>, EngineProcess<GameEngine> {

	float deltaTime = 0;

	@Override
	public void process(GameEntity entity) {
		entity.getComponent(ComponentType.PARTITION, PartitionComponent.class).update(deltaTime);
	}
	
	@Override
	public void process(GameEngine engine) {
		this.deltaTime = engine.getDeltaTime();
		engine.entitySystem.process(this, ComponentType.PARTITION);
	}

	@Override
	public void preProcess(GameEngine engine) {
		
	}

	@Override
	public void postProcess(GameEngine engine) {
		
	}

}
