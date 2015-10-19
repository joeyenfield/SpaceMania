package com.emptypockets.spacemania.engine.processes.host;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ai.AiComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class AiProcess implements SingleProcessor<GameEntity>, EngineProcess<GameEngine> {

	@Override
	public void preProcess(GameEngine engine) {
		
	}

	@Override
	public void process(GameEngine engine) {
		engine.entitySystem.process(this, ComponentType.AI);
	}

	@Override
	public void postProcess(GameEngine engine) {
		
	}

	@Override
	public void process(GameEntity entity) {
		entity.getComponent(ComponentType.AI, AiComponent.class).updateEntity();
	}


}
