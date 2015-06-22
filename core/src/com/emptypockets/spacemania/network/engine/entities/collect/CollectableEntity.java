package com.emptypockets.spacemania.network.engine.entities.collect;

import com.badlogic.gdx.graphics.Color;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

public abstract class CollectableEntity extends Entity {

	public CollectableEntity(EntityType type) {
		super(type);
		setLifeTime(10000);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		float progress = getAge() / lifeTime;
		if (progress > 0.8) {
			getColor().set(Color.ORANGE);
		} else {
			getColor().set(Color.GREEN).lerp(Color.YELLOW, progress);
		}
	}

	public abstract void collect(ServerPlayer player);

}
