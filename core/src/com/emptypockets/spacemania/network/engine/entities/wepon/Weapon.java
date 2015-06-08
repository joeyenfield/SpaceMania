package com.emptypockets.spacemania.network.engine.entities.wepon;

import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

public abstract class Weapon {

	public abstract void shoot(ServerPlayer player, PlayerEntity entity, ServerEngine engine);

}
