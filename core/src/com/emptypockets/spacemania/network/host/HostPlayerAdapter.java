package com.emptypockets.spacemania.network.host;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.GameEngineHost;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.network.client.ClientPlayerAdapter;
import com.emptypockets.spacemania.network.common.data.engine.GameEngineState;
import com.emptypockets.spacemania.utils.KryoUtils;
import com.emptypockets.spacemania.utils.PoolsManager;

public class HostPlayerAdapter implements Poolable {
	/*
	 * Client Specific Attributes
	 */
	public int clientId = -1;
	public int entityId = -1;
	public String username;
	public Rectangle region = new Rectangle();
	public HostDataSyncManager syncManager;

	static Boolean metrics = true;
	static int packetCount = 0;
	public static float dataRate = 0;
	public static long dataSize = 0;
	static long lastUpdate = 0;
	static long desiredUpdate = 1000;

	public ClientPlayerAdapter adapter;

	public void update(GameEngineHost engine) {
		if (syncManager == null) {
			syncManager = new HostDataSyncManager();
		}
		GameEntity ent = engine.entitySystem.getEntityById(entityId);
		if (ent == null) {
			ent = engine.entitySystem.pickRandom(GameEntityType.SHIP);
			if (ent != null) {
				entityId = ent.entityId;
			}
		}

		if (ent != null) {
			region.setCenter(ent.linearTransform.data.pos);
		}
		// Get Current State (Its flipped in the syncManager so it should be
		// save to free
		GameEngineState currentState = syncManager.getState(engine, region);
		sendState(currentState);
		PoolsManager.free(currentState);
	}

	public void sendState(GameEngineState state) {
		if (metrics) {
			dataSize += KryoUtils.getSize(state);
			packetCount++;

			long delta = System.currentTimeMillis() - lastUpdate;
			if (delta > desiredUpdate) {
				lastUpdate = System.currentTimeMillis();
				float deltaSec = delta / 1000f;
				dataRate = (dataSize / (float) deltaSec);
				System.out.println("DATA (NetworkManager.java:40):" + packetCount + " - " + dataRate);
				dataSize = 0;
				packetCount = 0;
			}
		}
		if (adapter != null) {
			System.out.println("SEND");
			System.out.println(state.entitySystemState.addedEntities.size());
			adapter.recieve(KryoUtils.clone(state));
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
