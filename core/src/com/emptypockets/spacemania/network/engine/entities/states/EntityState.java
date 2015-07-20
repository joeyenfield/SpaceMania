package com.emptypockets.spacemania.network.engine.entities.states;

import com.badlogic.gdx.math.Vector2;

public class EntityState {
	public int id;

	public Vector2 pos;
	public float ang;

	public EntityState() {
		pos = new Vector2();
	}

	/**
	 * Writes the current state on the object
	 * 
	 * @param state
	 */
	public void writeOnto(EntityState state) {
		id = state.id;
		state.pos.set(pos);
		state.ang = ang;
	}

	public void delta(float time) {
	}

	public float getAng() {
		return ang;
	}

	public void setAng(float ang) {
		this.ang = ang;
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
