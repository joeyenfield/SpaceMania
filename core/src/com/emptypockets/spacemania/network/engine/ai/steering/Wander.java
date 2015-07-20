package com.emptypockets.spacemania.network.engine.ai.steering;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.MovingEntity;

public class Wander extends Steering {

	float wanderRadius;
	float wanderDistance;
	float wanderJitter;
	Vector2 wanderVector = new Vector2();

	Vector2 temp = new Vector2();

	@Override
	public void updateSteeringForce(Engine engine, MovingEntity entity, Vector2 force) {

		// this behavior is dependent on the update rate, so this line must
		// be included when using time independent framerate.
		double JitterThisTimeSlice = wanderJitter;

		// first, add a small random vector to the target's position
		wanderVector.x += (1 - 2 * Math.random()) * JitterThisTimeSlice;
		wanderVector.y += (1 - 2 * Math.random()) * JitterThisTimeSlice;

		// reproject this new vec2tor back on to a unit circle
		wanderVector.nor();
		wanderVector.scl(wanderRadius);

		// move the target into a position WanderDist in front of the agent
		temp.set(entity.getVel());
		temp.nor();
		temp.scl(wanderDistance);
		temp.add(entity.getPos());
		temp.add(wanderVector);
		temp.sub(entity.getPos()).nor().scl(entity.getMaxVelocity());
		force.set(temp).sub(entity.getVel()).setLength(getMaxForce());
	}

	public float getWanderRadius() {
		return wanderRadius;
	}

	public void setWanderRadius(float wanderRadius) {
		this.wanderRadius = wanderRadius;
	}

	public float getWanderDistance() {
		return wanderDistance;
	}

	public void setWanderDistance(float wanderDistance) {
		this.wanderDistance = wanderDistance;
	}

	public float getWanderJitter() {
		return wanderJitter;
	}

	public void setWanderJitter(float wanderJitter) {
		this.wanderJitter = wanderJitter;
	}
}