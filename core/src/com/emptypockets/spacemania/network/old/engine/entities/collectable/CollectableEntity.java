package com.emptypockets.spacemania.network.old.engine.entities.collectable;

import com.badlogic.gdx.graphics.Color;
import com.emptypockets.spacemania.network.old.engine.entities.EntityType;
import com.emptypockets.spacemania.network.old.engine.entities.MovingEntity;
import com.emptypockets.spacemania.network.old.server.player.ServerPlayer;

public abstract class CollectableEntity extends MovingEntity {

	public CollectableEntity(EntityType type) {
		super(type);
		setLifeTime(10000);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		float progress = getAge() / (float)lifeTime;
		if (progress > 0.7) {
			getColor().set(Color.RED);
		} else {
			getColor().set(Color.GREEN).lerp(Color.RED, progress/0.7f);
		}
	}

	public abstract void collect(ServerPlayer player);

}
