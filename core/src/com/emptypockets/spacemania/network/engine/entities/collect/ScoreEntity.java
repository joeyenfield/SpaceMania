package com.emptypockets.spacemania.network.engine.entities.collect;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

public class ScoreEntity extends CollectableEntity {

	public ScoreEntity() {
		super(EntityType.Score);
		setMaxVelocity(50);
	}

	@Override
	public void collect(ServerPlayer player) {
		if (isAlive()) {
			player.addScore(this);
		}
		setAlive(false);
	}

	public void setPos(Vector2 pos) {
		getPos().set(pos);
	}

}
