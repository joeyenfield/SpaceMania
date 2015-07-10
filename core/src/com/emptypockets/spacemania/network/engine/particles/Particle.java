package com.emptypockets.spacemania.network.engine.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.network.engine.partitioning.cell.PartitionEntity;

public class Particle implements Poolable, PartitionEntity {
	Vector2 pos;
	Vector2 vel;
	Vector2 acl;
	private float angle = 0;
	boolean useVelAngle = true;
	long startTime;
	float progress;

	Interpolation scaleInterpolation = Interpolation.linear;
	float startScaleX = 1;
	float startScaleY = 1;
	float endScaleX = 0;
	float endScaleY = 0;
	float currentScaleX = 1;
	float currentScaleY = 1;

	Color startColor;
	Color endColor;
	Color currentColor;

	float radius = 1;
	long lifeTime = Constants.DEFAULT_PARTICLES_LIFETIME;
	float damping = 0.8f;

	ParticleType type;

	public Particle() {
		pos = new Vector2();
		vel = new Vector2();
		acl = new Vector2();

		startColor = new Color();
		endColor = new Color();
		currentColor = new Color();
		type = ParticleType.SPARK;
	}

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public void setScaleX(float start, float end) {
		this.startScaleX = start;
		this.endScaleX = end;
	}

	public void setScaleY(float start, float end) {
		this.startScaleY = start;
		this.endScaleY = end;
	}

	public void setupColor(Color start, Color end) {
		startColor.set(start);
		endColor.set(end);
	}

	public void update(float dt) {
		progress = (System.currentTimeMillis() - startTime) / (float) lifeTime;
		currentColor.set(startColor).lerp(endColor, progress);
		currentScaleX = scaleInterpolation.apply(startScaleX, endScaleX, progress);
		currentScaleY = scaleInterpolation.apply(startScaleY, endScaleY, progress);
		vel.x += acl.x * dt;
		vel.y += acl.y * dt;
		if (damping > 0) {
			vel.scl((float) Math.pow(damping, dt));
		}
		pos.x += vel.x * dt;
		pos.y += vel.y * dt;
		if (useVelAngle) {
			angle = vel.angle();
		}

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
		useVelAngle = true;
		type = ParticleType.SPARK;
		scaleInterpolation = Interpolation.linear;
		progress = 0;
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

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
		useVelAngle = false;
	}

	public void setUseVelAngle(boolean useVelAngle) {
		this.useVelAngle = useVelAngle;
	}

	public ParticleType getType() {
		return type;
	}

	public void setType(ParticleType type) {
		this.type = type;
	}

	public float getCurrentScaleX() {
		return currentScaleX;
	}

	public float getCurrentScaleY() {
		return currentScaleY;
	}

	public void setScaleInterpolation(Interpolation scaleInterpolation) {
		this.scaleInterpolation = scaleInterpolation;
	}

}
