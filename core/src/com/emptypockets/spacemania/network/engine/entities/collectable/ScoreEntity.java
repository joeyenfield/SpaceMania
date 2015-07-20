package com.emptypockets.spacemania.network.engine.entities.collectable;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

public class ScoreEntity extends CollectableEntity {
	float launchVel = 0;

	public ScoreEntity() {
		super(EntityType.Score);
		setMaxVelocity(500);
		setMaxForce(100000);
		setRadius(12);
		setLaunchVel(10);
	}

	@Override
	public void collect(ServerPlayer player) {
		if (isAlive()) {
			player.addScrapCount(this);
		}
		setAlive(false);
	}

	public void setPos(Vector2 pos) {
		getPos().set(pos);
	}

	public float getLaunchVel() {
		return launchVel;
	}

	public void setLaunchVel(float launchVel) {
		this.launchVel = launchVel;
	}

}
