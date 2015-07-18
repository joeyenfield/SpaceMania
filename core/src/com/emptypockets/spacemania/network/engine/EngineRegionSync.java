package com.emptypockets.spacemania.network.engine;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.client.payloads.engine.ClientRoomEngineRegionStatePayload;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.transport.ComsType;
import com.emptypockets.spacemania.utils.PoolsManager;

public class EngineRegionSync {
	Rectangle region;

	public void readFrom(ServerEngine engine) {
		region = engine.getRegion();
	}

	public void writeTo(ClientEngine engine) {
		engine.setRegion(region.x, region.y, region.width, region.height);
	}
}
