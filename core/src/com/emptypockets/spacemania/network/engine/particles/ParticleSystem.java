package com.emptypockets.spacemania.network.engine.particles;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.CleanerProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.engine.EntityManagerInterface;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.engine.entities.collect.CollectableEntity;
import com.emptypockets.spacemania.network.engine.partitioning.cell.CellSpacePartition;
import com.emptypockets.spacemania.utils.ColorUtils;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ParticleSystem extends ArrayListProcessor<Particle> implements EntityManagerInterface {
	int particleCountSphere = Constants.DEFAULT_PARTICLES_SPHERE;
	int maxParticles = Constants.DEFAULT_PARTICLES;
	CellSpacePartition<Particle> partition;
	ClientEngine engine;
	public ParticleSystem(ClientEngine engine) {
		partition = new CellSpacePartition<Particle>();
		partition.create(Constants.PARTICLE_SYTEM_PARTITION_X, Constants.PARTICLE_SYTEM_PARTITION_Y);
		this.engine=engine;
	}

	CleanerProcessor<Particle> deadCleanerProcessor;

	private void clearDead() {
		if (deadCleanerProcessor == null) {
			deadCleanerProcessor = new CleanerProcessor<Particle>() {
				@Override
				public boolean shouldRemove(Particle entity) {
					boolean dead = entity.isDead();
					if (dead) {
						PoolsManager.free(entity);
					}
					return dead;
				}
			};
		}
		process(deadCleanerProcessor);
	}

	public boolean hasMaxParticles() {
		return getSize() >= maxParticles;
	}

	public float fillFraction() {
		return (float) getSize() / (float) maxParticles;
	}

	private Particle getParticle() {
		return PoolsManager.obtain(Particle.class);
	}

	public void launchSparkSphere(Vector2 pos, int particleCount, Color start, Color end, boolean randomAngle) {
		if (hasMaxParticles()) {
			return;
		}

		Color color1 = null;
		Color color2 = null;
		if (start == null && end == null) {
			color1 = PoolsManager.obtain(Color.class);
			color2 = PoolsManager.obtain(Color.class);
		} else {
			color1 = start;
			color2 = end;
		}
		float angleStep = 360f / (particleCount);

		Vector2 angle = PoolsManager.obtain(Vector2.class).set(1, 0);
		Vector2 vel = PoolsManager.obtain(Vector2.class);
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
			launchSpark(pos, vel, color1, color2, randomAngle);
		}
		PoolsManager.free(angle);
		PoolsManager.free(vel);

		if (start == null && end == null) {
			PoolsManager.free(color1);
			PoolsManager.free(color2);
		}
	}

	public void launchSpark(Vector2 pos, Vector2 vel, Color start, Color end, boolean randomAngle) {
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
		if (randomAngle) {
			spark.setAngle(MathUtils.random(360));
		} else {
			spark.setUseVelAngle(true);
		}
		spark.start();
		spark.getPos().set(pos);
		spark.setupColor(start, end);
		spark.getVel().set(vel);
		spark.setScaleY(.3f, .3f);
		spark.setScaleX(.5f, 0.001f);
		spark.setLifeTime(MathUtils.random(3000, 5000));
		spark.setScaleInterpolation(Interpolation.fade);

		add(spark);
	}

	public void launchSmoke(Vector2 pos, Vector2 vel, Color start, Color end) {
		if (hasMaxParticles()) {
			return;
		}
		Particle spark = (Particle) getParticle();
		spark.setType(ParticleType.SMOKE);
		spark.setAngle(MathUtils.random(360));
		spark.start();
		spark.getPos().set(pos);
		spark.setupColor(start, end);
		spark.getVel().set(vel);
		spark.setScaleInterpolation(Interpolation.linear);
		spark.setScaleX(.01f, 2);
		spark.setScaleY(.01f, 2);
		spark.setLifeTime(3000 + MathUtils.random(2000));
		add(spark);
	}

	SingleProcessor<Particle> updateProcessor = null;
	public synchronized void update(){
		if(updateProcessor == null){
			updateProcessor = new SingleProcessor<Particle>() {
				@Override
				public void process(Particle entity) {
					entity.update(engine.getLastDeltaTime());
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
			};
		}
		process(updateProcessor);
		clearDead();
		partition.rebuild(this);
	}

	public void drawPlayerTrail(ClientEngine engine, PlayerEntity player) {
		player.createExhaust(engine, this);

	}

	@Override
	public void entityAdded(Entity entity) {
		// if (dynamicGrid && !(entity instanceof BulletEntity))
		// gridManager.applyExplosion(entity.getPos(), 2 * massSize, 800);
	}

	@Override
	public void entityRemoved(Entity entity, boolean killed) {
		if (!killed || !entity.isExplodes()) {
			return;
		}
		if (entity instanceof CollectableEntity) {
			return;
		}
		float multiplier = 1;
		boolean randomAngle = true;

		if (entity instanceof PlayerEntity) {
			multiplier = 100;
		} else if (entity instanceof BulletEntity) {
			multiplier = 0.2f;
			randomAngle = false;
		}
		Color colorA = new Color(entity.getColor());
		Color colorB = new Color(entity.getColor());
		colorB.a = 0.4f;
		colorA.a = 1f;
		launchSparkSphere(entity.getPos(), (int) (particleCountSphere * multiplier), colorA, colorB, randomAngle);

		// if (dynamicGrid && !(entity instanceof BulletEntity))
		// gridManager.applyExplosion(entity.getPos(), -10 * massSize, 800);
	}

	public void setMaxParticles(int maxParticles) {
		this.maxParticles = maxParticles;
	}

	public void getEntities(Rectangle viewport, ArrayList<Particle> renderEntities) {
		partition.getEntities(viewport, renderEntities);
	}

	public CellSpacePartition<Particle> getPartition() {
		return partition;
	}
}
