package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.math.Vector2;

public class EntityState {
	int id;

	Vector2 pos;
	Vector2 vel;

	float ang;
	float angVel;

	public EntityState() {
		pos = new Vector2();
		vel = new Vector2();
	}

	@Override
	public String toString() {
		return String.format("I[%3d] P[%6.3f,%6.3f] V[%6.3f,%6.3f]", id, pos.x, pos.y, vel.x, vel.y);
	}

	/**
	 * Writes the current state on the object
	 * @param state
	 */
	public void write(EntityState state) {
		state.pos.set(pos);
		state.vel.set(vel);
		state.ang = ang;
		state.angVel = angVel;
	}

	public void delta(float time) {
		pos.x += vel.x * time;
		pos.y += vel.y * time;

		ang += angVel * time;
	}

	public float getAng() {
		return ang;
	}

	public void setAng(float ang) {
		this.ang = ang;
	}

	public float getAngVel() {
		return angVel;
	}

	public void setAngVel(float angVel) {
		this.angVel = angVel;
	}

	public Vector2 getPos() {		
		return pos;
	}

	public Vector2 getVel() {
		return vel;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
