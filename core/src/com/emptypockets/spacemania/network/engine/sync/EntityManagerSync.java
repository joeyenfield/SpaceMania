package com.emptypockets.spacemania.network.engine.sync;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.client.payloads.engine.ClientEngineEntityManagerSyncPayload;
import com.emptypockets.spacemania.network.engine.EntityManagerInterface;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityState;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.transport.ComsType;

public class EntityManagerSync implements EntityManagerInterface, Poolable {
	long time;
	int roomId;
	private HashMap<Integer, EntityState> entityStates;
	ArrayList<EntityAdd> newEntities;
	ArrayList<EntityRemoval> removedEntities;
	boolean syncTime = false;

	public EntityManagerSync() {
		entityStates = new HashMap<Integer, EntityState>();
		newEntities = new ArrayList<EntityAdd>();
		removedEntities = new ArrayList<EntityRemoval>();
	}

	public void setSyncTime(boolean syncTime) {
		this.syncTime = syncTime;
	}

	public void writeToEngine(final ClientEngine engine) {
		if (syncTime) {
			engine.setTime(time);
			engine.setLastServerUpdateTime(time);
		}
		// Drop Old Packets
		if (engine.getLastServerUpdateTime() > time) {
			return;
		}
		engine.setLastServerUpdateTime(time);
		for (EntityAdd creation : newEntities) {
			Entity entity = engine.getEntityManager().createEntity(creation.getType(), creation.getId());
			creation.getEntityState().write(entity.getState());
			engine.getEntityManager().addEntity(entity);
		}

		for (EntityRemoval removal : removedEntities) {
			Entity entity = engine.getEntityManager().getEntityById(removal.getId());
			
			entity.getPos().set(removal.pos);
			entity.setExplodes(removal.isExplodes());
			
			engine.getEntityManager().removeEntityById(removal.getId(), removal.killed);
		}

		engine.getEntityManager().process(new SingleProcessor<Entity>() {

			@Override
			public void process(Entity entity) {
				EntityState serverState = entityStates.get(entity.getState().getId());
				StateSyncUtils.updateState(time, serverState, engine.getEngineLastUpdateTime(), entity.getState());
			}
		});

		releaseAddList();
		releaseRemovedList();
		releaseStateList();

	}

	@Override
	public synchronized void entityAdded(Entity entity) {
		EntityAdd created = Pools.obtain(EntityAdd.class);
		created.setId(entity.getState().getId());
		created.setType(entity.getType());
		created.setEntityState(entity.getState());
		newEntities.add(created);
		entityStates.put(entity.getState().getId(), entity.getState());
	}

	@Override
	public synchronized void entityRemoved(Entity entity, boolean killed) {
		EntityRemoval removed = Pools.obtain(EntityRemoval.class);
		removed.setId(entity.getState().getId());
		removed.setPos(entity.getState().getPos());
		removed.setKilled(killed);
		removed.setExplodes(entity.isExplodes());
		removedEntities.add(removed);
		entityStates.remove(entity.getState().getId());
	}

	private void releaseAddList() {
		for (EntityAdd creation : newEntities) {
			Pools.free(creation);
		}
		newEntities.clear();
	}

	private void releaseRemovedList() {
		for (EntityRemoval removal : removedEntities) {
			Pools.free(removal);
		}
		removedEntities.clear();
	}

	private void releaseStateList() {
		for (EntityState state : entityStates.values()) {
			Pools.free(state);
		}
		entityStates.clear();
	}

	@Override
	public void reset() {
		releaseAddList();
		releaseStateList();
		releaseRemovedList();
		syncTime = false;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public synchronized void broadcast(ServerPlayer player) {
		ClientEngineEntityManagerSyncPayload payload = Pools.obtain(ClientEngineEntityManagerSyncPayload.class);
		payload.setSyncData(this);
		payload.setComsType(ComsType.UDP);
		player.send(payload);
		Pools.free(payload);
		cleanAfterSends();
	}

	public synchronized void cleanAfterSends() {
		releaseAddList();
		releaseRemovedList();
	}
}
