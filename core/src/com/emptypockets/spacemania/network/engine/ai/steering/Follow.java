package com.emptypockets.spacemania.network.engine.ai.steering;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.Entity;

public class Follow extends ProximityTargetSteering {

	Vector2 desiredVelocity = new Vector2();

	@Override
	public void updateSteeringForce(Engine engine, Entity entity, Vector2 force) {
		desiredVelocity.set(target.getPos()).sub(entity.getPos()).nor().scl(entity.getMaxVelocity());
		force.set(desiredVelocity).sub(entity.getVel()).setLength(getMaxForce());
	}
}
