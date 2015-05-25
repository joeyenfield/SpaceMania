package com.emptypockets.spacemania.network.engine.grid.typeA;

import com.badlogic.gdx.math.Vector2;

public class PointMass {
	public Vector2 pos;
	public Vector2 vel;
	private Vector2 acl;
	public float inverseMass;
	private float damping = 1f;
	private float defaultDamping = 1f;
	public float time = 0;
	public PointMass(Vector2 position, float invMass) {
		pos = position;
		inverseMass = invMass;
		vel = new Vector2();
		acl = new Vector2();
	}

	public void print(){
		System.out.println(pos+"-"+vel+"-"+acl);
	}

	public void applyForce(Vector2 force) {
		acl.x += force.x * inverseMass;
		acl.y += force.y * inverseMass;
	}

	public void increaseDamping(float factor) {
		damping *= factor;
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
			vel.scl(damping);
		}
		damping = defaultDamping;
	}

	public void setDefaultDamping(float defaultDamping) {
		this.defaultDamping = defaultDamping;
	}
}