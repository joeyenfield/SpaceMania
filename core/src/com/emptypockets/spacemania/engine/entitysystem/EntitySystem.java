package com.emptypockets.spacemania.engine.entitysystem;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.MathUtils;
import com.emptypockets.spacemania.engine.entitysystem.processors.EntityMaskFilterProcessor;
import com.emptypockets.spacemania.engine.entitysystem.processors.EntityUpdateProcessor;
import com.emptypockets.spacemania.holders.ArrayListProcessor;

public class EntitySystem {

	HashMap<Integer, GameEntity> entitiesById = new HashMap<Integer, GameEntity>();
	ArrayListProcessor<GameEntity> entities = new ArrayListProcessor<GameEntity>();

	// Filters
	EntityMaskFilterProcessor maskFilterProcessor = new EntityMaskFilterProcessor();
	EntityUpdateProcessor updateProcessor = new EntityUpdateProcessor();

	public synchronized void add(GameEntity entity) {
		entities.add(entity);
		entitiesById.put(entity.entityId, entity);
	}

	public synchronized void remove(GameEntity entity) {
		entities.remove(entity);
		entitiesById.remove(entity.entityId);
	}

	public synchronized void update(float deltaTime) {
		updateProcessor.updateTime = deltaTime;
		try {
			entities.processParallel(updateProcessor, 4, 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// entities.process(updateProcessor);
	}

	public synchronized void filter(ArrayList<GameEntity> result, int abilityMask) {
		maskFilterProcessor.entities = result;
		maskFilterProcessor.abilityMask = abilityMask;
		entities.process(maskFilterProcessor);
		maskFilterProcessor.entities = null;
		maskFilterProcessor.abilityMask = 0;
	}

	public synchronized GameEntity getEntityById(int id) {
		return entitiesById.get(id);
	}

}
