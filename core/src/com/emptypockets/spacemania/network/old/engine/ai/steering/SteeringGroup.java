package com.emptypockets.spacemania.network.old.engine.ai.steering;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.old.engine.entities.MovingEntity;
import com.emptypockets.spacemania.network.old.server.engine.ServerEngine;

public class SteeringGroup {
	Vector2 force = new Vector2();
	ArrayList<Steering> steeringList;

	ServerEngine engine;
	MovingEntity entity;

	public SteeringGroup(ServerEngine engine, MovingEntity entity) {
		steeringList = new ArrayList<Steering>();
		this.engine = engine;
		this.entity = entity;
	}

	public void add(Steering steering) {
		steeringList.add(steering);
	}

	public void updateSteering() {
		for (Steering steering : steeringList) {
			steering.update(engine, entity);
		}
	}

	public void updateForce() {
		force.set(0, 0);
		for (Steering steering : steeringList) {
			force.add(steering.getSteeringForce());
		}
	}

	public Vector2 getForce() {
		return force;
	}
}
