package com.emptypockets.spacemania.network.engine.ai.steering;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.MovingEntity;

public class CoupledSteering extends Steering {

	
	
	@Override
	public void updateSteeringForce(Engine engine, MovingEntity entity, Vector2 force) {
		// TODO Auto-generated method stub
		
	}

}

class SteeringHold{
	public Steering steering;
	public float weight;
}
