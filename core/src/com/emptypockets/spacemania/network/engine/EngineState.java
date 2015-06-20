package com.emptypockets.spacemania.network.engine;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.client.payloads.engine.ClientEngineStatePayload;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

public class EngineState {
	Rectangle region;
	long time;

	public void readFrom(ServerEngine engine) {
		region = engine.getRegion();
		time = engine.getEngineLastUpdateTime();
	}

	public void writeTo(ClientEngine engine) {
		engine.setRegion(region.x, region.y, region.width, region.height);
	}

	public synchronized void broadcast(ServerPlayer player) {
		ClientEngineStatePayload payload = Pools.obtain(ClientEngineStatePayload.class);
		payload.setState(this);
		player.send(payload);
		Pools.free(payload);
	}
}
