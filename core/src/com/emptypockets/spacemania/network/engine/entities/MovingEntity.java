package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.entities.states.MovingEntityState;

public class MovingEntity extends Entity<MovingEntityState> {

	float maxVelocity = 100;
	float maxForce = 100;

	Vector2 lastPosition = new Vector2();
	Vector2 forceAculumator = new Vector2();
	Vector2 fovTemp1 = new Vector2();
	Vector2 fovTemp2 = new Vector2();

	float damping = 0;

	boolean bounceOffWalls = true; // Indicate if an enttity should stop dead or
									// bounce off walls

	public MovingEntity(EntityType type) {
		super(type, new MovingEntityState());
	}

	public void setPos(float x, float y) {
		lastPosition.set(getPos());
		super.setPos(x, y);
	}
	
	public boolean isInsideFOV(Entity ent, float fovInDeg) {
		fovTemp1.set(getVel()).nor();
		fovTemp2.set(ent.getPos()).sub(getPos()).nor();
		float angle = Math.abs(fovTemp1.angle(fovTemp2.nor()));
		return angle < fovInDeg / 2;
	}

	public void update(float deltaTime) {
		lastPosition.set(getPos());
		// Convert the Force to Velocity
		forceAculumator.limit(maxForce);
		forceAculumator.scl(inverseMass * deltaTime);

		// Update the velocity
		getVel().add(forceAculumator);
		if (damping > 0) {
			getVel().scl((float) Math.pow(damping, deltaTime));
		}
		getVel().limit(maxVelocity);
		forceAculumator.x = 0;
		forceAculumator.y = 0;

		super.update(deltaTime);

		lastMovementDist = lastPosition.dst(getPos());
	}

	public Vector2 getVel() {
		return state.getVel();
	}

	public float getMaxVelocity() {
		return maxVelocity;
	}

	public float getMaxForce() {
		return maxForce;
	}

	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}

	public void setDamping(float damping) {
		this.damping = damping;
	}

	public void resetForce() {
		forceAculumator.set(0, 0);
	}

	public void applyForce(Vector2 force) {
		this.forceAculumator.add(force);
	}

	public boolean isBounceOffWall() {
		return bounceOffWalls;
	}

	public void setBounceOffWalls(boolean boundeWalls) {
		this.bounceOffWalls = boundeWalls;
	}

	public void setVel(float x, float y) {
		getVel().set(x, y);
	}

	public void setVel(Vector2 vel) {
		getVel().set(vel);
	}

	public float getLastMovementDist() {
		return lastMovementDist;
	}

}
