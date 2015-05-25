package com.emptypockets.spacemania.network.engine.grid.typeA;

import com.badlogic.gdx.math.Vector2;

public class GridDistortion {
	public Vector2 pos;
	public Vector2 vel;
	public Vector2 acl;
	public float time = 0;
	public float inverseMass;
	public float force;
	public float radius;
	public float dampingFactor = 0.98f;
	
	public GridDistortion(Vector2 position, float inverseMass, float force, float radius) {
		time = System.currentTimeMillis();
		pos = position;
		vel = new Vector2();
		acl = new Vector2();
		this.force = force;
		this.radius = radius;
		this.inverseMass= inverseMass;
	}

	public void clearAcl(){
		acl.x = 0;
		acl.y = 0;
	}
	public void print(){
		System.out.println(pos+"-"+vel+"-"+acl);
	}

	public void applyForce(Vector2 force) {
		acl.x += force.x * inverseMass;
		acl.y += force.y * inverseMass;
	}

	public void increaseDamping(float factor) {
		dampingFactor *= factor;
	}

	public void update(float time) {
		vel.x += acl.x * time;
		vel.y += acl.y * time;

		pos.x += vel.x * time;
		pos.y += vel.y * time;

		acl.set(0, 0);
		if (vel.len2() < 0.00001f) {
			vel.set(0, 0);
		} else {
			vel.scl(dampingFactor);
		}
	}

	private float getForce() {
		return force;
	}

	private float getRadius() {
		return radius;
	}
}