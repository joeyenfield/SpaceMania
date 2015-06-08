package com.emptypockets.spacemania.network.engine.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Particle implements Poolable {
	Vector2 pos;
	Vector2 vel;
	Vector2 acl;

	long startTime;
	float progress;

	Color startColor;
	Color endColor;

	Color currentColor;

	float radius = 3;
	long lifeTime = 3000;
	float damping = 0.8f;

	public Particle() {
		pos = new Vector2();
		vel = new Vector2();
		acl = new Vector2();

		startColor = new Color();
		endColor = new Color();
		currentColor = new Color();
	}

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public void setupColor(Color start, Color end) {
		startColor.set(start);
		endColor.set(end);
	}

	public void update(float dt) {
		progress = (System.currentTimeMillis() - startTime) / (float) lifeTime;
		currentColor.set(startColor).lerp(endColor, progress);
		vel.x += acl.x * dt;
		vel.y += acl.y * dt;
		if (damping > 0) {
			vel.scl((float) Math.pow(damping, dt));
		}
		pos.x += vel.x * dt;
		pos.y += vel.y * dt;

	}

	public void setPos(float x, float y) {
		pos.x = x;
		pos.y = y;
	}

	public Vector2 getPos() {
		return pos;
	}

	public Vector2 getVel() {
		return vel;
	}

	public Color getCurrentColor() {
		return currentColor;
	}

	public boolean isDead() {
		boolean dead = progress > 1;
		return dead;
	}

	@Override
	public void reset() {
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}

}
