package com.emptypockets.spacemania.network.engine.ai.manager;

import com.emptypockets.spacemania.network.engine.ai.steering.Flee;
import com.emptypockets.spacemania.network.engine.ai.steering.Follow;
import com.emptypockets.spacemania.network.engine.ai.steering.Separation;
import com.emptypockets.spacemania.network.engine.ai.steering.Steering;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.entities.MovingEntity;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;

public class FollowerEntityAi extends EntityAi {

	Follow follow;
	Flee flee;
	Separation seperation;
	Steering currentSteering;

	MovingEntity target;

	public FollowerEntityAi(ServerEngine engine, MovingEntity entity) {
		super(engine, entity);
	}

	@Override
	public void setupSteering() {
		follow = new Follow();
		follow.setMaxForce(400);
		flee = new Flee();
		flee.setMaxForce(4000);
		seperation = new Separation();
		

	}

	@Override
	public void update() {
		//Check for any nearby bullets
		Entity entity = engine.getEntitySpatialPartition().searchNearestEntityWhereEntityInThereFOV(getEntity(), EntityType.Bullet, 200, 100);
		if (entity != null) {
			flee.setTarget(entity);
			currentSteering = flee;
			getEntity().setMaxVelocity(400);
			getEntity().setMaxForce(flee.getMaxForce());
		} else {
			if (target == null || !target.isAlive()) {
				follow.setTarget(null);
				target = (MovingEntity) engine.getEntityManager().pickRandom(EntityType.Player);
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
