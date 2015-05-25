package com.emptypockets.spacemania.network.engine.entities;


public class BulletEntity extends Entity {
	long creationTime = 0;
	long lifeTime = 3000;
	
	public BulletEntity(){
		super(EntityType.Bullet);
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

	@Override
	public boolean intersectsDetailed(Entity entity) {
		return false;
	}
}
