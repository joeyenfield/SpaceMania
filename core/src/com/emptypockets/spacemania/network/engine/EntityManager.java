package com.emptypockets.spacemania.network.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.emptypockets.spacemania.holders.ObjectProcessor;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;

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
			entity = new BulletEntity();
			((BulletEntity)entity).setCreationTime(System.currentTimeMillis());
			break;
		case Player:
			entity = new PlayerEntity();
			break;
		default:
			throw new RuntimeException("Unknown Entity Type");
		}
		
		entity.getState().setId(id);
		return entity;
	}

	public synchronized void addEntity(Entity entity) {
		entities.put(entity.getState().getId(), entity);
		notifyEntityAdded(entity);
	}

	public synchronized void removeEntity(Entity entity) {
		entities.remove(entity.getState().getId());
		notifyEntityRemoved(entity);
	}

	private void notifyEntityAdded(Entity entity) {
		synchronized (entityMangerInterface) {
			for (EntityManagerInterface mgrInterface : entityMangerInterface) {
				mgrInterface.entityAdded(entity);
			}
		}
	}

	private void notifyEntityRemoved(Entity entity) {
		synchronized (entityMangerInterface) {
			for (EntityManagerInterface mgrInterface : entityMangerInterface) {
				mgrInterface.entityRemoved(entity);
			}
		}
	}

	public void register(EntityManagerInterface managerInterface) {
		synchronized (entityMangerInterface) {
			entityMangerInterface.add(managerInterface);
		}
	}

	public void unregister(EntityManagerInterface managerInterface) {
		synchronized (entityMangerInterface) {
			entityMangerInterface.remove(managerInterface);
		}
	}

	@Override
	protected Iterator<Entity> getIterator() {
		return entities.values().iterator();
	}

	public synchronized void removeEntityById(int id) {
		entities.remove(id);
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
		for(Entity ent : entities.values()){
			if(!ent.isAlive()){
				toRemove.add(ent);
			}
		}
		
		for(Entity ent : toRemove){
			removeEntity(ent);
		}
	}

}
