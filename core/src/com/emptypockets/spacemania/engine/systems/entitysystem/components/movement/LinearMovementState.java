package com.emptypockets.spacemania.engine.systems.entitysystem.components.movement;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;

public class LinearMovementState extends ComponentState<LinearMovementState> {
	public Vector2 vel = new Vector2();
	public Vector2 acl = new Vector2();

	public float maxVel = 100;
	
	@Override
	public void readComponentState(LinearMovementState result) {
		result.vel.set(vel);
		result.acl.set(acl);
		result.maxVel = maxVel;
	}

	@Override
	public void writeComponentState(LinearMovementState data) {
		vel.set(data.vel);
		acl.set(data.acl);
		maxVel = data.maxVel;
	}

	@Override
	public boolean hasStateChanged(LinearMovementState data) {
		if (vel.dst2(data.vel) > 0.01) {
			return true;
		}

		if (acl.dst2(data.acl) > 0.01) {
			return true;
		}
		
		if(Math.abs(maxVel-data.maxVel) > 0.01){
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		vel.setZero();
		acl.setZero();
	}

}
