package com.emptypockets.spacemania.engine.processes.common;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.collission.CollissionComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class CollissionProcess implements EngineProcess<GameEngine> {
	float deltaTime = 0;
	ArrayList<GameEntity> entities = new ArrayList<GameEntity>();

	public void manage(EntitySystem entitySystem, float deltaTime) {
	}

	@Override
	public void process(GameEngine engine) {
		engine.entitySystem.filter(entities, ComponentType.COLLISSION);
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			GameEntity ent = entities.get(i);
			ent.getComponent(ComponentType.COLLISSION, CollissionComponent.class).update(deltaTime);
		}
		entities.clear();

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
