package com.emptypockets.spacemania.engine.systems.entitysystem.components.controls;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.weapon.WeaponComponent;
import com.emptypockets.spacemania.screens.GameEngineScreen;

public class ControlComponent extends EntityComponent<ControlData> {

	public ControlComponent() {
		super(ComponentType.CONTROL);
	}

	@Override
	public void setupData() {
		super.setupData();
		networkSync = true;
	}

	public void update(float deltaTime) {
		if (entity != null && entity.hasComponent(ComponentType.LINEAR_MOVEMENT)) {
			Vector2 vel = entity.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).data.vel;
			if (data.move.len2() > 0.01) {
				vel.set(data.move).scl(GameEngineScreen.maxVel);
			} else {
				vel.setZero();
			}
		}
		WeaponComponent weapon = entity.getComponent(ComponentType.WEAPON, WeaponComponent.class);
		if (weapon != null) {
			weapon.data.shooting = data.shooting;
		}
	}

	@Override
	public Class<ControlData> getDataClass() {
		return ControlData.class;
	}

}
