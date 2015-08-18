package com.emptypockets.spacemania.engine.processes.common;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystemManager;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.partition.PartitionComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class PartitionProcess extends EntitySystemManager implements SingleProcessor<GameEntity>, EngineProcess<GameEngine> {

	float deltaTime = 0;
	@Override
	public void manage(EntitySystem entitySystem, float deltaTime) {
		this.deltaTime = deltaTime;
		entitySystem.process(this, ComponentType.PARTITION);
	}

	@Override
	public void process(GameEntity entity) {
		entity.getComponent(ComponentType.PARTITION, PartitionComponent.class).update(deltaTime);
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
