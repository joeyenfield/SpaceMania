package com.emptypockets.spacemania.network.old.engine;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.network.old.client.ClientEngine;
import com.emptypockets.spacemania.network.old.client.payloads.engine.ClientRoomEngineRegionStatePayload;
import com.emptypockets.spacemania.network.old.server.engine.ServerEngine;
import com.emptypockets.spacemania.network.old.server.player.ServerPlayer;
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
