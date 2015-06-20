package com.emptypockets.spacemania.network.engine.ai.manager;

import com.emptypockets.spacemania.network.engine.ai.steering.Flee;
import com.emptypockets.spacemania.network.engine.ai.steering.Follow;
import com.emptypockets.spacemania.network.engine.ai.steering.ProximityTargetSteering.ProximityType;
import com.emptypockets.spacemania.network.engine.ai.steering.Steering;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;

public class FollowerEntityAi extends EntityAi {

	Follow follow;
	Flee flee;

	Steering currentSteering;

	Entity target;

	public FollowerEntityAi(ServerEngine engine, Entity entity) {
		super(engine, entity);
	}

	@Override
	public void setupSteering() {
		follow = new Follow();
		follow.setMaxForce(400);
		flee = new Flee();
		flee.setMaxForce(4000);
	}

	@Override
	public void update() {
		Entity entity = engine.getEntitySpatialPartition().searchNearestEntityWhereEntityInThereFOV(getEntity(), EntityType.Bullet, 200, 100);
		if (entity != null) {
			flee.setTarget(entity);
			currentSteering = flee;
			getEntity().setMaxVelocity(400);
			getEntity().setMaxForce(flee.getMaxForce());
		} else {
			if (target == null || !target.isAlive()) {
				target = engine.getEntityManager().pickRandom(EntityType.Player);
				follow.setTarget(target);
			}
			currentSteering = follow;
			getEntity().setMaxVelocity(200);
			getEntity().setMaxForce(follow.getMaxForce());
		}

		currentSteering.update(getEngine(), getEntity());
	}

	@Override
	public void apply() {
		getEntity().applyForce(currentSteering.getSteeringForce());
	}

}
