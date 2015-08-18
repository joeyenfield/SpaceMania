package com.emptypockets.spacemania.engine.processes.common;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystemManager;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction.DestructionComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.utils.PoolsManager;

public class DestructionProcess extends EntitySystemManager implements SingleProcessor<GameEntity>, EngineProcess<GameEngine> {
	float deltaTime = 0;
	ArrayList<GameEntity> entities = new ArrayList<GameEntity>();

	@Override
	public void manage(EntitySystem entitySystem, float deltaTime) {
		entitySystem.process(this, ComponentType.DESTRUCTION);

		int size = entities.size();
		for (int i = 0; i < size; i++) {
			GameEntity ent = entities.get(i);
			DestructionComponent comp = ent.getComponent(ComponentType.DESTRUCTION, DestructionComponent.class);
			comp.update(deltaTime);
			if (comp.data.remove) {
				ent.engine.removeEntity(ent);
			}

		}
		entities.clear();
	}

	@Override
	public void process(GameEntity entity) {
		entities.add(entity);
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
