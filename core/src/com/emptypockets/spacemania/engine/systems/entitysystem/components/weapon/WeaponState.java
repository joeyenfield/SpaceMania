package com.emptypockets.spacemania.engine.systems.entitysystem.components.weapon;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;

public class WeaponState extends ComponentState<WeaponState> {
	public long shootTime = 2000;
	public float bulletLife = 2;
	public float bulletVel = 400;
	public boolean shooting = false;
	public Vector2 shootDir = new Vector2();
	
	@Override
	public void readComponentState(WeaponState result) {
	}

	@Override
	public void writeComponentState(WeaponState data) {
	}

	@Override
	public boolean hasStateChanged(WeaponState data) {
		return false;
	}

	@Override
	public void reset() {
		shooting = false;
		shootDir.setZero();
	}

}
