package com.emptypockets.spacemania.network.engine.entities.particles;

import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;

public class Spark extends Entity {
	long creationTime;
	long lifeTime = 10000;

	public Spark() {
		super(EntityType.Particle);

		setRadius(1f);
		setMaxVelocity(600);
		setLifeTime(3000);
		setDamping(0.6f);
	}

	public void updateCreationTime() {
		creationTime = System.currentTimeMillis();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		float age = System.currentTimeMillis() - creationTime;
		float life = 1 - age / lifeTime;
		float alpha = Math.max(0.05f, life);
		if (life < 0) {
			setAlive(false);
		} else {
			color.a = alpha;
		}
		if (getState().getVel().len2() > 1) {
			// float oldAngle = state.ang;
			// float newAngle = state.vel.angle();
			getState().setAng(getState().getVel().angle());
		}
	}

	public long getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}

}
