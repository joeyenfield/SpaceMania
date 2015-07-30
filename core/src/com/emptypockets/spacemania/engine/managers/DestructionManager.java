package com.emptypockets.spacemania.engine.managers;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystemManager;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.destruction.DestructionComponent;
import com.emptypockets.spacemania.engine.spatialpartition.CellsGameEntitySpatitionPartition;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.utils.PoolsManager;

public class DestructionManager extends EntitySystemManager implements SingleProcessor<GameEntity> {
	float deltaTime = 0;
	ArrayList<GameEntity> entities = new ArrayList<GameEntity>();

	@Override
	public void manage(EntitySystem entitySystem, float deltaTime) {
		entitySystem.process(this, ComponentType.DESTRUCTION);
		
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			GameEntity ent = entities.get(i);
			ent.getComponent(ComponentType.DESTRUCTION).update(deltaTime);
		}
	}

	@Override
	public void process(GameEntity entity) {
		entities.add(entity);
	}

	public void removeEntities(CellsGameEntitySpatitionPartition spatial, EntitySystem entitySystem) {
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			GameEntity ent = entities.get(i);
			if (ent.getComponent(ComponentType.DESTRUCTION, DestructionComponent.class).data.remove) {
				entitySystem.remove(ent);
				spatial.removeEntity(ent);
				PoolsManager.free(ent);
			}
		}
		entities.clear();
	}

}
