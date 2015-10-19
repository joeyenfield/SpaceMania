package com.emptypockets.spacemania.engine.network.host;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.GameEngineHost;
import com.emptypockets.spacemania.engine.input.PlayerInputData;
import com.emptypockets.spacemania.engine.network.client.ClientPlayerAdapter;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.controls.ControlComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.weapon.WeaponComponent;
import com.emptypockets.spacemania.network.common.data.engine.GameEngineState;
import com.emptypockets.spacemania.screens.GameEngineScreen;
import com.emptypockets.spacemania.utils.KryoUtils;
import com.emptypockets.spacemania.utils.PoolsManager;
import com.emptypockets.spacemania.utils.RectangeUtils;

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

	public PlayerInputData lastInput = new PlayerInputData();

	public synchronized void processOutgoingPackets(GameEngineHost engine) {
		if (syncManager == null) {
			syncManager = new HostDataSyncManager();
		}

		// Get Current State (Its flipped in the syncManager so it should be
		// save to free
		GameEngineState currentState = syncManager.getState(engine, region);
		currentState.myPlayerId = entityId;
		sendState(currentState);
		PoolsManager.free(currentState);
	}

	public synchronized void update(GameEngineHost engine) {
		GameEntity ent = engine.entitySystem.getEntityById(entityId);
		if (ent == null) {
			//Add new ship entity
			ent = engine.createEntity(GameEntityType.SHIP);
			entityId = ent.entityId;
			ent.addComponent(ComponentType.CONTROL);
			RectangeUtils.randomPoint(engine.universeRegion, ent.linearTransform.state.pos);
		}

		if (ent != null) {
			region.setCenter(ent.linearTransform.state.pos);
			if (ent.hasComponent(ComponentType.CONTROL)) {
				//Apply last movement state
				ControlComponent comp = ent.getComponent(ComponentType.CONTROL, ControlComponent.class);
				comp.state.move.set(lastInput.move);
				comp.state.shootDir.set(lastInput.shootDir);
				comp.state.shooting = lastInput.shoot;
			} else {
				engine.println("Entity is missing control compoenent (HostPlayerAdapter.java:70) ");
			}
		}
	}

	private void sendState(GameEngineState state) {
		if (adapter != null) {
			adapter.recieve(KryoUtils.clone(state));
		}

		if (metrics) {
			dataSize += KryoUtils.getSize(state);
			packetCount++;

			long delta = System.currentTimeMillis() - lastUpdate;
			if (delta > desiredUpdate) {
				lastUpdate = System.currentTimeMillis();
				float deltaSec = delta / 1000f;
				dataRate = (dataSize / (float) deltaSec);
				// System.out.println("DATA (HostPlayerAdapter.java:40):" + packetCount + " - " + dataRate);
				dataSize = 0;
				packetCount = 0;
			}
		}
	}

	public synchronized void recieve(Object data) {
		if (data instanceof PlayerInputData) {
			PlayerInputData input = (PlayerInputData) data;
			lastInput.move.set(input.move);
			lastInput.shootDir.set(input.shootDir);
			lastInput.shoot = input.shoot;
			PoolsManager.free(input);
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
