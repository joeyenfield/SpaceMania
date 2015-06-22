package com.emptypockets.spacemania.network.engine.ai.manager;

import com.emptypockets.spacemania.network.engine.ai.steering.Wander;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;

public class RandomEntityAi extends EntityAi {

	public RandomEntityAi(ServerEngine engine, Entity entity) {
		super(engine, entity);
	}

	Wander wander;

	@Override
	public void setupSteering() {
		wander = new Wander();
		wander.setMaxForce(5000);
		wander.setWanderDistance(0);
		wander.setWanderRadius(100);
		wander.setWanderJitter(30);
		getEntity().setMaxVelocity(200);

	}

	@Override
	public void update() {
		wander.update(getEngine(), getEntity());

	}

	@Override
	public void apply() {
		getEntity().applyForce(wander.getSteeringForce());
	}

}
