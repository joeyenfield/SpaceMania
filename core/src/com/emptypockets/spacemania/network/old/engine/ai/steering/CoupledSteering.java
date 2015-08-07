package com.emptypockets.spacemania.network.old.engine.ai.steering;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.old.engine.Engine;
import com.emptypockets.spacemania.network.old.engine.entities.MovingEntity;

public class CoupledSteering extends Steering {
	Vector2 forceTmp = new Vector2();
	ArrayList<SteeringHold> steering = new ArrayList<SteeringHold>();

	public void addSteering(float weight, Steering dat) {
		SteeringHold hold = new SteeringHold();
		hold.weight = weight;
		hold.steering = dat;

		steering.add(hold);
	}

	@Override
	public void updateSteeringForce(Engine engine, MovingEntity entity, Vector2 force) {
		force.setZero();
		int size = steering.size();
		for (int i = 0; i < size; i++) {
			SteeringHold hold = steering.get(i);
			forceTmp.setZero();
			hold.steering.updateSteeringForce(engine, entity, forceTmp);
			forceTmp.scl(hold.weight);
			force.add(forceTmp);
		}
	}

}

class SteeringHold {
	public Steering steering;
	public float weight;
}
