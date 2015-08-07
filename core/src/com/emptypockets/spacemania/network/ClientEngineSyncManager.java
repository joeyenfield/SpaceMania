package com.emptypockets.spacemania.network;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.GameEntityType;
import com.emptypockets.spacemania.network.transport.data.engine.GameEngineState;
import com.emptypockets.spacemania.utils.KryoUtils;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ClientEngineSyncManager implements Poolable {
	/*
	 * Client Specific Attributes
	 */
	public int clientId = -1;
	public int entityId = -1;
	public String username;
	public Rectangle region = new Rectangle();
	public EngineSyncManager syncManager;

	static Boolean metrics = true;
	static int packetCount = 0;
	public static float dataRate = 0;
	public static long dataSize = 0;
	static long lastUpdate = 0;
	static long desiredUpdate = 1000;

	public void update(GameEngine engine) {
		if (syncManager == null) {
			syncManager = new EngineSyncManager();
		}
		GameEntity ent = engine.entitySystem.getEntityById(entityId);
		if (ent == null) {
			ent = engine.entitySystem.pickRandom(GameEntityType.SHIP);
			entityId = ent.entityId;
		}
		region.setCenter(ent.linearTransform.data.pos);

		// Get Current State
		GameEngineState currentState = syncManager.getState(engine, region);
		sendState(currentState);
		Pools.free(currentState);
	}

	public void sendState(GameEngineState state) {
		dataSize += KryoUtils.getSize(state);
		packetCount++;
		
		long delta = System.currentTimeMillis() - lastUpdate;
		if (delta > desiredUpdate) {
			lastUpdate = System.currentTimeMillis();
			float deltaSec = delta/1000f;
			dataRate = (dataSize/(float)deltaSec);
			System.out.println("DATA (NetworkManager.java:40):"+packetCount+" - "+dataRate);
			dataSize = 0;
			packetCount = 0;
		}
	}

	@Override
	public void reset() {
		PoolsManager.free(region);
		PoolsManager.free(syncManager);
		clientId = 0;
		username = null;
		region = null;
		syncManager = null;
	}

}
