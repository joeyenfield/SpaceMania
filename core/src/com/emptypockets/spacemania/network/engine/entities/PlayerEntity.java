package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.client.ClientEngine;

public class PlayerEntity extends Entity {
	long lastExhaust = 0;
	long exhaustTime = 500;
	Color sideExhauseColor = new Color(200 / 256f, 38 / 256f, 9 / 256f, 1); // deep
	Color midExhauseColor = new Color(255 / 256f, 187 / 256f, 30 / 256f, 1); // orange-yellow

	public PlayerEntity() {
		super(EntityType.Player);
		setColor(Color.GREEN);
		setDamping(0);
		setMaxVelocity(500);
		setRadius(30);
		setBounceOffWalls(false);
	}

	// TODO Auto-generated method stub
	public void createExhaust(ClientEngine engine) {
		if (getVel().len2() > 0.1f) {
			// set up some variables

			Quaternion rot = new Quaternion();
			rot.setEulerAngles(0f, 0f, getVel().angle());

			float t = engine.getTime();
			// The primary velocity of the particles is 3 pixels/frame in the
			// direction opposite to which the ship is travelling.
			Vector2 baseVel = getVel().cpy().scl(-3);
			// Calculate the sideways velocity for the two side streams. The
			// direction is perpendicular to the ship's velocity and the
			// magnitude varies sinusoidally.
			Vector2 perpVel = new Vector2(baseVel.y, -baseVel.x);
			perpVel.scl((0.6f * (float) MathUtils.sin(t * 10)));

			float alpha = 0.7f;

			// // middle particle stream
			 Vector2 velMid = baseVel.cpy();
			 velMid.y += MathUtils.random();
			 
//			 engine.getParticleSystem().launchSpark(pos, vel, start, end);
			// GameRoot.ParticleManager.CreateParticle(Art.LineParticle, pos,
			// Color.White * alpha, 60f, new Vector2(0.5f, 1),
			// new ParticleState(velMid, ParticleType.Enemy));
			// GameRoot.ParticleManager.CreateParticle(Art.Glow, pos, midColor *
			// alpha, 60f, new Vector2(0.5f, 1),
			// new ParticleState(velMid, ParticleType.Enemy));
			//
			// // side particle streams
			// Vector2 vel1 = baseVel + perpVel + rand.NextVector2(0, 0.3f);
			// Vector2 vel2 = baseVel - perpVel + rand.NextVector2(0, 0.3f);
			// GameRoot.ParticleManager.CreateParticle(Art.LineParticle, pos,
			// Color.White * alpha, 60f, new Vector2(0.5f, 1),
			// new ParticleState(vel1, ParticleType.Enemy));
			// GameRoot.ParticleManager.CreateParticle(Art.LineParticle, pos,
			// Color.White * alpha, 60f, new Vector2(0.5f, 1),
			// new ParticleState(vel2, ParticleType.Enemy));
			//
			// GameRoot.ParticleManager.CreateParticle(Art.Glow, pos, sideColor
			// * alpha, 60f, new Vector2(0.5f, 1),
			// new ParticleState(vel1, ParticleType.Enemy));
			// GameRoot.ParticleManager.CreateParticle(Art.Glow, pos, sideColor
			// * alpha, 60f, new Vector2(0.5f, 1),
			// new ParticleState(vel2, ParticleType.Enemy));
		}
		if (System.currentTimeMillis() - lastExhaust > exhaustTime) {
			lastExhaust = System.currentTimeMillis();
		}
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (state.vel.len2() > 1) {
			state.ang = state.vel.angle();
		}
	}

	public void addScore() {
		// TODO Auto-generated method stub
		
	}

}
