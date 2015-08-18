package com.emptypockets.spacemania.engine.systems.entitysystem.components.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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

	public void update(float deltaTime) {
		data.move.setZero();
		if (Gdx.input.isKeyPressed(Keys.W)) {
			data.move.y = 1;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			data.move.y = -1;
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			data.move.x = -1;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			data.move.x = 1;
		}

		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			data.shooting = true;
		} else {
			data.shooting = false;
		}

		Vector2 vel = entity.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).data.vel;
		if (data.move.len2() > 0.01) {
			vel.set(data.move).scl(GameEngineScreen.maxVel);
		} else {
			vel.setZero();
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
