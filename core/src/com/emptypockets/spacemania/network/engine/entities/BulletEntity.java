package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.graphics.Color;

public class BulletEntity extends Entity {
	PlayerEntity owner;

	public BulletEntity() {
		super(EntityType.Bullet);
		setRadius(10);
		setColor(Color.ORANGE);
		setDamping(0);
		setMaxVelocity(1000);
		setBounceOffWalls(false);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (state.vel.len2() > 1) {
			state.ang = state.vel.angle();
		}
	}

	public void setOwner(PlayerEntity owner) {
		this.owner = owner;
	}

	public PlayerEntity getOwner() {
		return owner;
	}

	
}
