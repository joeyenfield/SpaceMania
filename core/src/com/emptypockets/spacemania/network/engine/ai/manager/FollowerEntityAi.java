package com.emptypockets.spacemania.network.engine.ai.manager;

import com.emptypockets.spacemania.network.engine.ai.steering.CoupledSteering;
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

	CoupledSteering steeringFlee;
	CoupledSteering steeringFollow;

	MovingEntity target;

	public FollowerEntityAi(ServerEngine engine, MovingEntity entity) {
		super(engine, entity);
	}

	@Override
	public void setupSteering() {
		follow = new Follow();
		flee = new Flee();
		
		seperation = new Separation();
		
		steeringFlee = new CoupledSteering();
		steeringFlee.addSteering(100, flee);
//		steeringFlee.addSteering(1, seperation);

		steeringFollow = new CoupledSteering();
		steeringFollow.addSteering(1, follow);
		steeringFollow.addSteering(1, seperation);

	}

	@Override
	public void update() {
		// Check for any nearby bullets
		Entity entity = engine.getEntitySpatialPartition().searchNearestEntityWhereEntityInThereFOV(getEntity(), EntityType.Bullet, 200, 100);
		if (entity != null) {
			flee.setTarget(entity);
			currentSteering = steeringFlee;
			getEntity().setMaxVelocity(400);
		} else {
			if (target == null || !target.isAlive()) {
				follow.setTarget(null);
				target = (MovingEntity) engine.getEntityManager().pickRandom(EntityType.Player);
				follow.setTarget(target);
			}
			getEntity().setMaxVelocity(200);
			currentSteering = steeringFollow;
		}
		currentSteering.update(getEngine(), getEntity());
	}

	@Override
	public void apply() {
		getEntity().applyForce(currentSteering.getSteeringForce());
	}

}
