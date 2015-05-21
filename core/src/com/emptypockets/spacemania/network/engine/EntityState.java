package com.emptypockets.spacemania.network.engine;

import com.badlogic.gdx.math.Vector2;

public class EntityState {
	int id;

	Vector2 pos;
	Vector2 vel;
	Vector2 acl;

	float ang;
	float angVel;
	float angAcl;

	public EntityState() {
		pos = new Vector2();
		vel = new Vector2();
		acl = new Vector2();
	}

	@Override
	public String toString() {
		return String.format("P[%6.3f,%6.3f] V[%6.3f,%6.3f]", pos.x, pos.y, vel.x, vel.y);
	}

	public void write(EntityState state) {
		state.pos.set(pos);
		state.vel.set(vel);
		state.acl.set(acl);

		state.ang = ang;
		state.angVel = angVel;
	}

	public float getAngAcl() {
		return angAcl;
	}

	public void delta(float time) {
		vel.x += acl.x * time;
		vel.y += acl.y * time;

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

	public Vector2 getAcl() {
		return acl;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
