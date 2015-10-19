package com.emptypockets.spacemania.engine.processes.common;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction.DestructionComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class DestructionProcess implements EngineProcess<GameEngine> {
	float deltaTime = 0;
	ArrayList<GameEntity> entities = new ArrayList<GameEntity>();

	@Override
	public void process(GameEngine engine) {
		engine.entitySystem.filter(entities, ComponentType.DESTRUCTION);

		int size = entities.size();
		for (int i = 0; i < size; i++) {
			GameEntity ent = entities.get(i);
			DestructionComponent comp = ent.getComponent(ComponentType.DESTRUCTION, DestructionComponent.class);
			comp.update(deltaTime);
			if (comp.state.remove) {
				ent.engine.removeEntity(ent);
			}

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
