package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.client.ClientEngine;
import com.emptypockets.spacemania.network.client.input.ClientInput;
import com.emptypockets.spacemania.network.engine.particles.ParticleSystem;

public class PlayerEntity extends Entity {
	long lastExhaust = 0;
	long exhaustTime = 25;
	float magnetDistance = 100;
	Color smokeStart = new Color(1,1,1, 0.3f); // deep
	Color smokeEnd = new Color(.1f,.1f,.1f, 0.01f); // orange-yellow

	public PlayerEntity() {
		super(EntityType.Player);
		setColor(Color.GREEN);
		setDamping(0);
		setMaxVelocity(400);
		setRadius(25);
		setBounceOffWalls(false);
	}

	public void applyClientInput(ClientInput input) {
		getVel().set(input.getMove()).limit2(1).scl(getMaxVelocity());
	}

	// TODO Auto-generated method stub
	public void createExhaust(ClientEngine engine, ParticleSystem particleSys) {
		if (getVel().len2() > 0.1f) {
			// set up some variables
			if (System.currentTimeMillis() - lastExhaust > exhaustTime) {
				lastExhaust = System.currentTimeMillis();
				Vector2 baseVel = getVel().cpy().scl(-.1f);
				particleSys.launchSmoke(getPos(), baseVel, smokeStart, smokeEnd);
				baseVel.scl(.3f);
				particleSys.launchSmoke(getPos(), baseVel, smokeStart, smokeEnd);
			}
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

	public float getMagnetDistance() {
		return magnetDistance;
	}

	public void setMagnetDistance(float magnetDistance) {
		this.magnetDistance = magnetDistance;
	}

}
