package com.emptypockets.spacemania.network.engine.ai.steering;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.MovingEntity;

public abstract class Steering {
	float maxForce = 200;
	Vector2 steeringForce = new Vector2();

	public void update(Engine engine, MovingEntity entity) {
		updateSteeringForce(engine, entity, steeringForce);
	}

	public abstract void updateSteeringForce(Engine engine, MovingEntity entity, Vector2 force);

	public Vector2 getSteeringForce() {
		return steeringForce;
	}

	public float getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}

}
