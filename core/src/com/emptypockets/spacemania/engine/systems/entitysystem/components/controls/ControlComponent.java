package com.emptypockets.spacemania.engine.systems.entitysystem.components.controls;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.weapon.WeaponComponent;
import com.emptypockets.spacemania.screens.GameEngineScreen;

public class ControlComponent extends EntityComponent<ControlState> {

	public ControlComponent() {
		super(ComponentType.CONTROL);
	}

	@Override
	public void setupState() {
		super.setupState();
		networkSync = true;
	}

	public void update(float deltaTime) {
		if (entity != null && entity.hasComponent(ComponentType.LINEAR_MOVEMENT)) {
			Vector2 vel = entity.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).state.vel;
			if (state.move.len2() > 0.01) {
				vel.set(state.move).scl(GameEngineScreen.velShip);
			} else {
				vel.setZero();
			}
		}
		WeaponComponent weapon = entity.getComponent(ComponentType.WEAPON, WeaponComponent.class);
		if (weapon != null) {
			weapon.state.shooting = state.shooting;
			weapon.state.shootDir.set(state.shootDir);
		}
	}

	@Override
	public Class<ControlState> getStateClass() {
		return ControlState.class;
	}

}
