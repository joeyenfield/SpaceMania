package com.emptypockets.spacemania.network.old.engine.grid;

import com.badlogic.gdx.math.Vector2;

public class GridNode {
	public float inverseMass;
	public Vector2 restPos;
	public Vector2 pos;
	public Vector2 vel;
	public Vector2 acl;

	float damping = 0.98f;
	float defaultDamping = 0.98f;

	//
	public GridNode() {
		pos = new Vector2();
		restPos = new Vector2();
		vel = new Vector2();
		acl = new Vector2();
		inverseMass = 0;
	}

	public void resetRestPos() {
		restPos.set(pos);
	}

	public void applyImpulse(Vector2 impulse) {
		vel.x += impulse.x * inverseMass;
		vel.y += impulse.y * inverseMass;
	}

	public void applyForce(Vector2 force) {
		acl.x += force.x * inverseMass;
		acl.y += force.y * inverseMass;
	}

	public float getEnergy() {
		return 0.5f * 1 / inverseMass * vel.len2();
	}

	public String format(Vector2 v) {
		return String.format("[%5.0f,%5.0f]", v.x, v.y);
	}

	public void update() {
		vel.x += acl.x;
		vel.y += acl.y;

		pos.x += vel.x;
		pos.y += vel.y;

		vel.x *= damping;
		vel.y *= damping;
		damping = defaultDamping;

		if (vel.len2() < 1e-5f) {
			vel.x = 0;
			vel.y = 0;
		}
		acl.x = 0;
		acl.y = 0;

		if (Float.isNaN(pos.x)) {
			pos.x = restPos.x;
			vel.x = 0;
		}

		if (Float.isNaN(pos.y)) {
			pos.y = restPos.y;
			vel.y = 0;
		}

	}

	public void increaseDamping(float factor) {
		this.damping *= factor;
	}

}