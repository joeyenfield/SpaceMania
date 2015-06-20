package com.emptypockets.spacemania.network.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.holders.ObjectProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.EnemyEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.engine.entities.collect.ScoreEntity;
import com.emptypockets.spacemania.network.engine.partitioning.cell.CellSpacePartition;
import com.sun.corba.se.spi.ior.iiop.MaxStreamFormatVersionComponent;

public class EntityManager extends ObjectProcessor<Entity> {
	int entityCount = 0;
	HashMap<Integer, Entity> entities;

	ArrayList<EntityManagerInterface> entityMangerInterface;

	public EntityManager() {
		entities = new HashMap<Integer, Entity>();
		entityMangerInterface = new ArrayList<EntityManagerInterface>();
	}

	public synchronized Entity createEntity(EntityType type) {
		entityCount++;
		return createEntity(type, entityCount);
	}

	public synchronized Entity createEntity(EntityType type, int id) {
		Entity entity;
		switch (type) {
		case Bullet:
			entity = Pools.obtain(BulletEntity.class);
			break;
		case Player:
			entity = Pools.obtain(PlayerEntity.class);
			break;
		case Score:
			entity = Pools.obtain(ScoreEntity.class);
			break;
		case Enemy_FOLLOW:
			entity = Pools.obtain(EnemyEntity.class);
			entity.setType(EntityType.Enemy_FOLLOW);
			break;
		case Enemy_RANDOM:
			entity = Pools.obtain(EnemyEntity.class);
			entity.setType(EntityType.Enemy_RANDOM);
			break;
		default:
			throw new RuntimeException("Unknown Entity Type");
		}

		entity.getState().setId(id);
		entity.tagCreationTime();
		return entity;
	}

	public synchronized void addEntity(Entity entity) {
		entities.put(entity.getState().getId(), entity);
		notifyEntityAdded(entity);
	}

	public synchronized void removeEntity(Entity entity, boolean killed) {
		entities.remove(entity.getState().getId());
		notifyEntityRemoved(entity, killed);
		Pools.free(entity);
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
			//Silently remove all entites
			process(new SingleProcessor<Entity>() {
				@Override
				public void process(Entity entity) {
					managerInterface.entityRemoved(entity, false);
				}
			});
		}
	}

	@Override
	protected Iterator<Entity> getIterator() {
		return entities.values().iterator();
	}

	public synchronized void clear() {
		entities.clear();
	}

	public int getSize() {
		return entities.size();
	}

	public Entity getEntityById(int entityId) {
		return entities.get(entityId);
	}

	public synchronized void removeDead() {
		final ArrayList<Entity> toRemove = new ArrayList<Entity>();
		for (Entity ent : entities.values()) {
			if (!ent.isAlive()) {
				toRemove.add(ent);
			}
		}

		for (Entity ent : toRemove) {
			removeEntity(ent, true);
		}
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

	public synchronized <T> ArrayList<T> filterEntities(Class<T> type) {
		ArrayList<T> result = new ArrayList<T>();
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

}
