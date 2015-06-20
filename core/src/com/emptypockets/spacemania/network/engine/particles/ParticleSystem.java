package com.emptypockets.spacemania.network.engine.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.CleanerProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.engine.EntityManagerInterface;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.engine.entities.collect.CollectableEntity;
import com.emptypockets.spacemania.utils.ColorUtils;

public class ParticleSystem extends ArrayListProcessor<Particle> implements EntityManagerInterface {

	int particleCountSphere = 50;
	int maxParticles = 10000;

	public ParticleSystem() {
	}

	private void clearDead() {
		process(new CleanerProcessor<Particle>() {
			@Override
			public boolean shouldRemove(Particle entity) {
				boolean dead = entity.isDead();
				if (dead) {
					Pools.free(entity);
				}
				return dead;
			}
		});
	}

	public boolean hasMaxParticles() {
		return getSize() >= maxParticles;
	}

	public float fillFraction() {
		return (float) getSize() / (float) maxParticles;
	}

	public void launchSpark(Vector2 pos, Vector2 vel, Color start, Color end) {
		if (hasMaxParticles()) {
			return;
		}
		float fillFract = fillFraction();
		if (fillFract > 0.5f) {
			if (MathUtils.random() < fillFract) {
				return;
			}
		}
		Particle spark = (Particle) getParticle();
		spark.start();
		spark.getPos().set(pos);
		spark.setupColor(start, end);
		spark.getVel().set(vel);
		spark.setLifeTime(MathUtils.random(3000, 5000));
		add(spark);
	}

	public void launchSphere(Vector2 pos, int particleCount, Color start, Color end) {
		if (hasMaxParticles()) {
			return;
		}

		Color color1 = null;
		Color color2 = null;
		if (start == null && end == null) {
			color1 = Pools.obtain(Color.class);
			color2 = Pools.obtain(Color.class);
		} else {
			color1 = start;
			color2 = end;
		}
		float angleStep = 360f / (particleCount);

		Vector2 angle = Pools.obtain(Vector2.class).set(1, 0);
		Vector2 vel = Pools.obtain(Vector2.class);
		for (int i = 0; i < particleCount; i++) {
			if (start == null && end == null) {
				float hue1 = MathUtils.random(2, 6);
				float hue2 = (hue1 + MathUtils.random(0, 2)) % 6f;
				ColorUtils.HSVToColor(color1, hue1, 0.5f, 1);
				ColorUtils.HSVToColor(color2, hue2, 0.5f, 1);
				color2.a = 0;
			}
			angle.rotate(MathUtils.random(0.5f, 2) * angleStep);
			vel.set(angle).scl(MathUtils.random(200, 500));
			launchSpark(pos, vel, color1, color2);
		}
		Pools.free(angle);
		Pools.free(vel);

		if (start == null && end == null) {
			Pools.free(color1);
			Pools.free(color2);
		}
	}

	private Particle getParticle() {
		return Pools.obtain(Particle.class);
	}

	public synchronized void update(final ClientEngine engine, final float dt) {
		process(new SingleProcessor<Particle>() {
			@Override
			public void process(Particle entity) {
				entity.update(dt);
				Rectangle region = engine.getRegion();
				Vector2 pos = entity.getPos();
				float rad = entity.getRadius();
				float inset = rad + 2;
				if (pos.x - rad < region.x) {
					entity.setPos(region.x + inset, pos.y);
					entity.getVel().x *= -1;
				}
				if (pos.x + rad > region.x + region.width) {
					entity.setPos(region.x + region.width - inset, pos.y);
					entity.getVel().x *= -1;
				}
				if (pos.y - rad < region.y) {
					entity.setPos(pos.x, region.y + inset);
					entity.getVel().y *= -1;
				}
				if (pos.y + rad > region.y + region.height) {
					entity.setPos(pos.x, region.y + region.height - inset);
					entity.getVel().y *= -1;
				}
			}
		});
		clearDead();
	}

	public void drawPlayerTrail(PlayerEntity player) {
		// player.createExhaust(this);

	}

	@Override
	public void entityAdded(Entity entity) {
		// if (dynamicGrid && !(entity instanceof BulletEntity))
		// gridManager.applyExplosion(entity.getPos(), 2 * massSize, 800);
	}

	@Override
	public void entityRemoved(Entity entity, boolean killed) {
		if(!killed){
			return;
		}
		if (entity instanceof CollectableEntity) {
			return;
		}
		float multiplier = 1;
		if (entity instanceof PlayerEntity) {
			multiplier = 100;
		}else if(entity instanceof BulletEntity){
			multiplier  = 0.2f;
		}
		Color colorA = new Color(entity.getColor());
		Color colorB = new Color(entity.getColor());
		colorB.a = 0.4f;
		colorA.a = 1f;
		launchSphere(entity.getPos(), (int)(particleCountSphere * multiplier), colorA, colorB);

		// if (dynamicGrid && !(entity instanceof BulletEntity))
		// gridManager.applyExplosion(entity.getPos(), -10 * massSize, 800);
	}

	public void setMaxParticles(int maxParticles) {
		this.maxParticles = maxParticles;
	}
}
