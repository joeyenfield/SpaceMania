package com.emptypockets.spacemania.engine.systems.entitysystem.components.weapon;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentData;

public class WeaponData extends ComponentData<WeaponData> {
	public long shootTime = 2000;
	public float bulletLife = 2;
	public float bulletVel = 400;
	public boolean shooting = false;

	@Override
	public void getComponentData(WeaponData result) {
	}

	@Override
	public void setComponentData(WeaponData data) {
	}

	@Override
	public boolean changed(WeaponData data) {
		return false;
	}

	@Override
	public void reset() {
	}

}
