package com.emptypockets.spacemania.network.engine.sync;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.metrics.plotter.DataLogger;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.client.payloads.engine.ClientEngineEntityManagerSyncPayload;
import com.emptypockets.spacemania.network.engine.EntityManagerInterface;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.states.EntityState;
import com.emptypockets.spacemania.network.engine.sync.events.EntityAdd;
import com.emptypockets.spacemania.network.engine.sync.events.EntityRemoval;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.transport.ComsType;
import com.emptypockets.spacemania.utils.PoolsManager;

public class EntityManagerSync implements EntityManagerInterface, Poolable {
	long time;
	int roomId;
	ArrayList<EntityState> entityStates;
	ArrayList<EntityAdd> newEntities;
	ArrayList<EntityRemoval> removedEntities;

	public EntityManagerSync() {
		entityStates = new ArrayList<EntityState>();
		newEntities = new ArrayList<EntityAdd>();
		removedEntities = new ArrayList<EntityRemoval>();
	}

	public void writeToEngine(final ClientEngine engine, boolean syncTime) {
		if (syncTime) {
			engine.setTime(time);
		}
		engine.setLastServerUpdateTime(time);

		for (EntityAdd creation : newEntities) {
			Entity entity = engine.getEntityManager().createEntity(creation.getType(), creation.getId());
			creation.getEntityState().writeOnto(entity.getState());
			engine.getEntityManager().addEntity(entity);
		}

		for (EntityRemoval removal : removedEntities) {
			Entity entity = engine.getEntityManager().getEntityById(removal.getId());
			entity.getPos().set(removal.pos);
			entity.setExplodes(removal.isExplodes());
			engine.getEntityManager().removeEntityById(removal.getId(), removal.killed);
		}

		for (EntityState state : entityStates) {
			Entity entity = engine.getEntityManager().getEntityById(state.getId());
			StateSyncUtils.updateState(time, state, engine.getEngineLastUpdateTime(), entity, false);
		}
		releaseAddList();
		releaseRemovedList();
		releaseStateList();

	}

	@Override
	public synchronized void entityAdded(Entity entity) {
		EntityAdd created = PoolsManager.obtain(EntityAdd.class);
		created.setId(entity.getState().getId());
		created.setType(entity.getType());
		created.setEntityState(entity.getState());
		newEntities.add(created);
		entityStates.add(entity.getState());
	}

	@Override
	public synchronized void entityRemoved(Entity entity, boolean killed) {
		EntityRemoval removed = PoolsManager.obtain(EntityRemoval.class);
		removed.setId(entity.getState().getId());
		removed.setPos(entity.getState().getPos());
		removed.setKilled(killed);
		removed.setExplodes(entity.isExplodes());
		removedEntities.add(removed);
		entityStates.remove(entity.getState());
	}
	
	public void clearAfterDataSend(){
		releaseAddList();
		releaseRemovedList();
	}

	private void releaseAddList() {
		int size = newEntities.size();
		for (int i = 0; i < size; i++) {
			PoolsManager.free(newEntities.get(i));
		}
		newEntities.clear();
	}

	private void releaseRemovedList() {
		int size = removedEntities.size();
		for (int i = 0; i < size; i++) {
			PoolsManager.free(removedEntities.get(i));
		}
		removedEntities.clear();
	}

	private void releaseStateList() {
		for (EntityState state : entityStates) {
			PoolsManager.free(state);
		}
		entityStates.clear();
	}

	@Override
	public void reset() {
		releaseAddList();
		releaseStateList();
		releaseRemovedList();
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
}
