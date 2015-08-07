package com.emptypockets.spacemania.network.old.engine.ai.steering;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.old.engine.Engine;
import com.emptypockets.spacemania.network.old.engine.entities.MovingEntity;

public class Follow extends ProximityTargetSteering {

	Vector2 desiredVelocity = new Vector2();

	@Override
	public void updateSteeringForce(Engine engine, MovingEntity entity, Vector2 force) {
		if (target != null) {
			desiredVelocity.set(target.getPos()).sub(entity.getPos()).nor().scl(entity.getMaxVelocity());
			force.set(desiredVelocity).sub(entity.getVel()).setLength(getMaxForce());
		}else{
			force.setZero();
		}
	}
}
