package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class Entity implements Poolable {
	Polygon shape;
	EntityState state;
	EntityType type;
	boolean alive = true;
	boolean reflectWall = true;

	public void setType(EntityType type) {
		this.type = type;
	}

	protected Color color;
	float inverseMass = 1;
	float radius = 15;
	float maxVelocity = 100;
	float maxForce = 100;
	Vector2 lastPosition = new Vector2();
	float lastDist = 0;
	float damping = 0;

	public Entity(EntityType type) {
		this.type = type;
		state = new EntityState();
		color = Color.GREEN.cpy();
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean contact(Entity entity) {
		boolean contact = false;
		float radSize = getRadius() + entity.getRadius();
		float interactRadius = radSize + lastDist + entity.lastDist;
		float dist2 = getPos().dst2(entity.getPos());

		if (dist2 < radSize * radSize) {
			contact = true;
		} else if (dist2 > interactRadius * interactRadius) {
			contact = false;
		} else {
			if (Intersector.intersectSegmentCircle(lastPosition, getPos(), entity.getPos(), radSize * radSize)) {
				contact = true;
			} else if (Intersector.intersectSegmentCircle(entity.lastPosition, entity.getPos(), getPos(), radSize * radSize)) {
				contact = true;
			}
		}
		return contact;
	}

	public void setPos(float x, float y) {
		lastPosition.set(getPos());
		state.getPos().x = x;
		state.getPos().y = y;
	}

	public float getRadius() {
		return radius;
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
		lastPosition.set(getPos());
		state.delta(deltaTime);
		if (damping > 0) {
			getVel().scl((float) Math.pow(damping, deltaTime));
		}
		getVel().limit(maxVelocity);
		lastDist = lastPosition.dst(getPos());
	}

	public float getMaxVelocity() {
		return maxVelocity;
	}

	public float getInverseMass() {
		return inverseMass;
	}

	public float getMaxForce() {
		return maxForce;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color.cpy();
	}

	public void setInverseMass(float inverseMass) {
		this.inverseMass = inverseMass;
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

	@Override
	public void reset() {
		alive = true;
	}

	public boolean isReflectWall() {
		return reflectWall;
	}

	public void setReflectWall(boolean reflectWall) {
		this.reflectWall = reflectWall;
	}
}
