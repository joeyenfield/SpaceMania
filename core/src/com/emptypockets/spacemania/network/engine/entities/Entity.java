package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	Polygon shape;
	EntityState state;
	EntityType type;
	boolean alive = true;
	Rectangle aaBounds;
	public Entity(EntityType type) {
		this.type = type;
		state = new EntityState();
		aaBounds = new Rectangle();
		shape = new Polygon();
	}
	
	public abstract boolean intersectsDetailed(Entity entity);
	public boolean intersectsAABounds(Entity entity){
		return aaBounds.overlaps(entity.aaBounds);
	}

	public Rectangle getAABounds(){
		return aaBounds;
	}
	
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
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
