package com.emptypockets.spacemania.network.engine.ai.steering;

import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.Entity;

public abstract class ProximityTargetSteering extends Steering {

	public static enum ProximityType {
		ALWAYS, NEAR, FAR
	};

	ProximityType proximityType = ProximityType.ALWAYS;
	float distance = 0;

	Entity target;

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public ProximityType getProximityType() {
		return proximityType;
	}

	public void setProximityType(ProximityType proximityType) {
		this.proximityType = proximityType;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public void update(Engine engine, Entity entity) {
		if (target != null && target.isAlive()) {
			super.update(engine, entity);

			switch (proximityType) {
			case NEAR:
				if (entity.getPos().dst2(target.getPos()) > distance * distance) {
					getSteeringForce().set(0, 0);
					return;
				}
				break;
			case FAR:
				if (entity.getPos().dst2(target.getPos()) < distance * distance) {
					getSteeringForce().set(0, 0);
					return;
				}
				break;
			case ALWAYS:
				break;
			}
			super.update(engine, entity);
		} else {
			getSteeringForce().set(0, 0);
		}
	}
}
