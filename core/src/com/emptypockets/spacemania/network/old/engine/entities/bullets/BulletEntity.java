package com.emptypockets.spacemania.network.old.engine.entities.bullets;

import com.badlogic.gdx.graphics.Color;
import com.emptypockets.spacemania.network.old.engine.entities.EntityType;
import com.emptypockets.spacemania.network.old.engine.entities.MovingEntity;
import com.emptypockets.spacemania.network.old.engine.entities.players.PlayerEntity;

public class BulletEntity extends MovingEntity {
	PlayerEntity owner;

	public BulletEntity() {
		super(EntityType.Bullet);
		setColor(Color.ORANGE);
		setRadius(10);
		setDamping(0);
		setMaxVelocity(500);
		setBounceOffWalls(false);
		setLifeTime(2000);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (state.vel.len2() > 1) {
			state.ang = state.vel.angle();
		}
		getColor().a = (1 - .8f*getLifeProgress());
	}

	public void setOwner(PlayerEntity owner) {
		this.owner = owner;
	}

	public PlayerEntity getOwner() {
		return owner;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		super.reset();
		color.a = 1;
	}

}
