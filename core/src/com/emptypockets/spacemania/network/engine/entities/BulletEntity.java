package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.graphics.Color;


public class BulletEntity extends Entity {
	public BulletEntity() {
		super(EntityType.Bullet);
		setRadius(10);
		setColor(Color.ORANGE);
		setDamping(0);
		setMaxVelocity(800);
		setReflectWall(false);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (state.vel.len2() > 1) {
			state.ang = state.vel.angle();
		}
	}
}
