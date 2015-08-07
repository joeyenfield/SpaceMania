package com.emptypockets.spacemania.network.old.engine.entities.states;

import com.badlogic.gdx.math.Vector2;

public class MovingEntityState extends EntityState {

	public float angVel;
	public Vector2 vel;

	public MovingEntityState() {
		vel = new Vector2();
	}

	public void delta(float time) {
		super.delta(time);
		pos.x += vel.x * time;
		pos.y += vel.y * time;
		ang += angVel * time;
	}

	public float getAngVel() {
		return angVel;
	}

	public void setAngVel(float angVel) {
		this.angVel = angVel;
	}

	public Vector2 getVel() {
		return vel;
	}

	/**
	 * Writes the current state on the object
	 * 
	 * @param state
	 */
	public void writeOnto(MovingEntityState state) {
		super.writeOnto(state);
		state.vel.set(vel);
		state.angVel = angVel;
	}

}
