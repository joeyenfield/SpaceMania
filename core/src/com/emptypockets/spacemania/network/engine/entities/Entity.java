package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.EntityState;
import com.emptypockets.spacemania.network.engine.EntityType;

public class Entity {
	EntityState state;
	EntityType type;
	boolean alive = true;

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Entity() {
		state = new EntityState();
	}

	public Vector2 getPos() {
		return state.getPos();
	}

	public Vector2 getVel() {
		return state.getVel();
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public EntityState getState() {
		return state;
	}

	public int getId() {
		return state.getId();
	}

	public void update(float deltaTime) {
		state.delta(deltaTime);
	}

	public Vector2 getAcl() {
		return state.getAcl();
	}

}
