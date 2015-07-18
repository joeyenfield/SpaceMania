package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;


public class EnemyEntity extends Entity{
	public EnemyEntity() {
		super(EntityType.Enemy_RANDOM);
		setColor(Color.RED);
		setRadius(20);
	}
}
