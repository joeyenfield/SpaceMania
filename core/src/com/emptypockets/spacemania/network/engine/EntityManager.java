package com.emptypockets.spacemania.network.engine;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.EnemyEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.engine.entities.collect.ScoreEntity;
import com.emptypockets.spacemania.utils.PoolsManager;

public class EntityManager {
	int entityCount = 0;
	HashMap<Integer, Entity> entities;
	ArrayListProcessor<Entity> processor;
	ArrayList<EntityManagerInterface> entityMangerInterface;
	final ArrayList<Entity> tempToRemove = new ArrayList<Entity>();

	public EntityManager() {
		entities = new HashMap<Integer, Entity>();
		entityMangerInterface = new ArrayList<EntityManagerInterface>();
		processor = new ArrayListProcessor<Entity>();
	}

	public synchronized Entity createEntity(EntityType type) {
		entityCount++;
		return createEntity(type, entityCount);
	}

	public synchronized Entity createEntity(EntityType type, int id) {
		Entity entity;
		switch (type) {
		case Bullet:
			entity = PoolsManager.obtain(BulletEntity.class);
			break;
		case Player:
			entity = PoolsManager.obtain(PlayerEntity.class);
			break;
		case Score:
			entity = PoolsManager.obtain(ScoreEntity.class);
			break;
		case Enemy_FOLLOW:
			entity = PoolsManager.obtain(EnemyEntity.class);
			entity.setType(EntityType.Enemy_FOLLOW);
			entity.setColor(Color.CYAN);
			break;
		case Enemy_RANDOM:
			entity = PoolsManager.obtain(EnemyEntity.class);
			entity.setType(EntityType.Enemy_RANDOM);
			entity.setColor(Color.MAGENTA);
			break;
		default:
			throw new RuntimeException("Unknown Entity Type");
		}
		entity.setAlive(true);
		entity.getState().setId(id);
		entity.tagCreationTime();
		return entity;
	}

	public synchronized void addEntity(Entity entity) {
		entities.put(entity.getState().getId(), entity);
		processor.add(entity);
		notifyEntityAdded(entity);
	}

	public synchronized void removeEntity(Entity entity, boolean killed) {
		entities.remove(entity.getState().getId());
		processor.remove(entity);
		notifyEntityRemoved(entity, killed);
		PoolsManager.free(entity);
	}

	public synchronized void removeEntityById(int id, boolean killed) {
		Entity entity = getEntityById(id);
		if (entity != null) {
			removeEntity(entity, killed);
		}
	}

	private void notifyEntityAdded(Entity entity) {
		synchronized (entityMangerInterface) {
			for (EntityManagerInterface mgrInterface : entityMangerInterface) {
				mgrInterface.entityAdded(entity);
			}
		}
	}

	private void notifyEntityRemoved(Entity entity, boolean killed) {
		synchronized (entityMangerInterface) {
			for (EntityManagerInterface mgrInterface : entityMangerInterface) {
				mgrInterface.entityRemoved(entity, killed);
			}
		}
	}

	public synchronized void register(final EntityManagerInterface managerInterface) {
		synchronized (entityMangerInterface) {
			entityMangerInterface.add(managerInterface);
			process(new SingleProcessor<Entity>() {
				@Override
				public void process(Entity entity) {
					managerInterface.entityAdded(entity);
				}
			});
		}
	}

	public synchronized void unregister(final EntityManagerInterface managerInterface) {
		synchronized (entityMangerInterface) {
			entityMangerInterface.remove(managerInterface);
			// Silently remove all entites
			process(new SingleProcessor<Entity>() {
				@Override
				public void process(Entity entity) {
					managerInterface.entityRemoved(entity, false);
				}
			});
		}
	}

	public synchronized void clear() {
		entities.clear();
		processor.clear();
	}

	public int getSize() {
		return entities.size();
	}

	public Entity getEntityById(int entityId) {
		return entities.get(entityId);
	}

	public synchronized void removeDead() {
		for (Entity ent : entities.values()) {
			if (!ent.isAlive()) {
				tempToRemove.add(ent);
			}
		}

		for (Entity ent : tempToRemove) {
			removeEntity(ent, true);
		}
		tempToRemove.clear();
	}

	public synchronized int countType(EntityType entType) {
		int count = 0;
		for (Entity ent : entities.values()) {
			if (ent.getType() == entType) {
				count++;
			}
		}
		return count;
	}

	public synchronized <T> ArrayList<T> filterEntities(Class<T> type, ArrayList<T> result) {
		
		for (Entity ent : entities.values()) {
			if (type.isAssignableFrom(ent.getClass())) {
				result.add((T) ent);
			}
		}
		return result;
	}

	public synchronized Entity pickRandom(EntityType entType) {
		ArrayList<Entity> players = new ArrayList<Entity>();
		for (Entity ent : entities.values()) {
			if (ent.getType() == entType) {
				players.add((PlayerEntity) ent);
			}
		}
		if (players.size() == 0) {
			return null;
		}
		return players.get(MathUtils.random(players.size() - 1));
	}

	public void process(SingleProcessor<Entity> singleProcessor) {
		processor.process(singleProcessor);
	}

	public ArrayListProcessor<Entity> getProcessor() {
		return processor;
	}

}
