package com.emptypockets.spacemania.network.engine.entities;

import com.emptypockets.spacemania.network.engine.EntityType;

public class BulletEntity extends Entity {
	long creationTime = 0;
	long lifeTime = 3000;
	
	public BulletEntity(){
		setType(EntityType.Bullet);
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public void update(float deltaTime) {
		super.update(deltaTime);
		if (System.currentTimeMillis() - creationTime > lifeTime) {
			setAlive(false);
		}
	}
}
