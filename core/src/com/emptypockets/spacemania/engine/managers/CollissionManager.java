package com.emptypockets.spacemania.engine.managers;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystemManager;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.collission.CollissionComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class CollissionManager extends EntitySystemManager implements SingleProcessor<GameEntity> {
	float deltaTime = 0;
	ArrayList<GameEntity> entities = new ArrayList<GameEntity>();

	@Override
	public void manage(EntitySystem entitySystem, float deltaTime) {
		entitySystem.process(this, ComponentType.COLLISSION);

		int size = entities.size();
		for (int i = 0; i < size; i++) {
			GameEntity ent = entities.get(i);
			ent.getComponent(ComponentType.COLLISSION, CollissionComponent.class).update(deltaTime);
		}
		entities.clear();
	}

	@Override
	public void process(GameEntity entity) {
		entities.add(entity);
	}
}
