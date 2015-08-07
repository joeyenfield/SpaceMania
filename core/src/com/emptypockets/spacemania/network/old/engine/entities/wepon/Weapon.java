package com.emptypockets.spacemania.network.old.engine.entities.wepon;

import com.emptypockets.spacemania.network.old.engine.entities.players.PlayerEntity;
import com.emptypockets.spacemania.network.old.server.engine.ServerEngine;
import com.emptypockets.spacemania.network.old.server.player.ServerPlayer;

public abstract class Weapon {

	public abstract void shoot(ServerPlayer player, PlayerEntity entity, ServerEngine engine);

}
