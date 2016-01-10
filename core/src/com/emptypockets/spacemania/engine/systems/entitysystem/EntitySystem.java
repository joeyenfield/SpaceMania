package com.emptypockets.spacemania.engine.systems.entitysystem;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Bits;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.processors.EntityMaskArrayListProcessor;
import com.emptypockets.spacemania.engine.systems.entitysystem.processors.EntityMaskCollectProcessor;
import com.emptypockets.spacemania.engine.systems.entitysystem.processors.EntityMaskFilterProcessor;
import com.emptypockets.spacemania.engine.systems.entitysystem.processors.EntityRegionCollectProcessor;
import com.emptypockets.spacemania.engine.systems.entitysystem.processors.EntityRegionFilterProcessor;
import com.emptypockets.spacemania.engine.systems.entitysystem.processors.EntityTypeCountProcessor;
import com.emptypockets.spacemania.engine.systems.entitysystem.processors.filter.GameEntityFilterProcessor;
import com.emptypockets.spacemania.engine.systems.entitysystem.processors.filter.GameEntityTypeFilter;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.utils.PoolsManager;

public class EntitySystem implements EntityDestructionListener {

	HashMap<Integer, GameEntity> entitiesById = new HashMap<Integer, GameEntity>(1000);
	ArrayListProcessor<GameEntity> entities = new ArrayListProcessor<GameEntity>(1000);

	public synchronized void add(GameEntity entity) {
		entities.add(entity);
		entitiesById.put(entity.entityId, entity);
		entity.addListener(this);
	}

	public synchronized void remove(GameEntity entity, boolean detatchListener) {
		entities.remove(entity);
		entitiesById.remove(entity.entityId);
		if (detatchListener) {
			entity.removeListener(this);
		}
	}

	public synchronized GameEntity getEntityById(int id) {
		return entitiesById.get(id);
	}

	public int getEntityCount() {
		return entities.getSize();
	}

	public synchronized int getEntityCount(GameEntityType enemy) {
		// Filters
		EntityTypeCountProcessor counter = PoolsManager.obtain(EntityTypeCountProcessor.class);
		counter.type = enemy;
		entities.process(counter);
		int rst = counter.count;
		PoolsManager.free(counter);
		return rst;
	}

	public void filter(ArrayListProcessor<GameEntity> result, Rectangle region) {
		filter(result, region, null);
	}

	public void filter(ArrayList<GameEntity> result, ComponentType type) {
		filter(result, type.getMask());
	}

	public void filter(ArrayList<GameEntity> result, Bits abilityMask) {
		// Filters
		EntityMaskArrayListProcessor maskFilterProcessor = PoolsManager.obtain(EntityMaskArrayListProcessor.class);
		maskFilterProcessor.entities = result;
		maskFilterProcessor.abilityMask = abilityMask;
		entities.process(maskFilterProcessor);
		PoolsManager.free(maskFilterProcessor);
	}

	public void filter(ArrayListProcessor<GameEntity> result, Bits abilityMask) {
		// Filters
		EntityMaskCollectProcessor maskFilterProcessor = PoolsManager.obtain(EntityMaskCollectProcessor.class);
		maskFilterProcessor.entities = result;
		maskFilterProcessor.abilityMask = abilityMask;
		entities.process(maskFilterProcessor);
		PoolsManager.free(maskFilterProcessor);
	}

	public void filter(ArrayListProcessor<GameEntity> result, Rectangle region, Bits abilityMask) {
		EntityRegionCollectProcessor regionProcessor = PoolsManager.obtain(EntityRegionCollectProcessor.class);
		regionProcessor.entities = result;
		regionProcessor.abilityMask = abilityMask;
		regionProcessor.region = region;
		entities.process(regionProcessor);
		PoolsManager.free(regionProcessor);
	}

	public void process(SingleProcessor<GameEntity> processor, Rectangle region, Bits abilityMask) {
		EntityRegionFilterProcessor regionProcessor = PoolsManager.obtain(EntityRegionFilterProcessor.class);
		regionProcessor.processor = processor;
		regionProcessor.abilityMask = abilityMask;
		regionProcessor.region = region;
		entities.process(regionProcessor);
		PoolsManager.free(regionProcessor);
	}

	public void process(SingleProcessor<GameEntity> processor, Bits abilityMask) {
		EntityMaskFilterProcessor filterProcessor = PoolsManager.obtain(EntityMaskFilterProcessor.class);
		filterProcessor.abilityMask = abilityMask;
		filterProcessor.processor = processor;
		entities.process(filterProcessor);
		PoolsManager.free(filterProcessor);
	}

	public void process(SingleProcessor<GameEntity> processor, ComponentType type) {
		process(processor, type.getMask());
	}

	public void process(SingleProcessor<GameEntity> processor) {
		entities.process(processor);
	}

	public GameEntity pickRandom(GameEntityType type) {
		GameEntityTypeFilter typeFilter = PoolsManager.obtain(GameEntityTypeFilter.class);
		typeFilter.type = type;

		GameEntityFilterProcessor processor = PoolsManager.obtain(GameEntityFilterProcessor.class);
		processor.filter = typeFilter;
		processor.filter(this);
		GameEntity entity = processor.pickRandom();
		PoolsManager.free(processor);
		return entity;
	}

	@Override
	public void entityDestruction(GameEntity entity) {
		remove(entity, false);
	}

}
