package com.emptypockets.spacemania.engine.systems.entitysystem.components.weapon;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.collission.CollissionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction.DestructionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;

public class WeaponComponent extends EntityComponent<WeaponState> {

	long lastShoot = 0;

	public WeaponComponent() {
		super(ComponentType.WEAPON);
	}

	public void update(float deltaTime) {
		if (System.currentTimeMillis() - lastShoot > state.shootTime && state.shooting) {
			lastShoot = System.currentTimeMillis();

			GameEntity bullet = entity.engine.createEntity(GameEntityType.BULLET);
			WeaponComponent weaponComp = entity.getComponent(ComponentType.WEAPON, WeaponComponent.class);
			DestructionComponent desComp = bullet.getComponent(ComponentType.DESTRUCTION, DestructionComponent.class);
			if (desComp != null) {
				desComp.state.destroyTime = entity.engine.getTime() + (weaponComp.state.bulletLife);
			}
			Vector2 pos = bullet.linearTransform.state.pos;
			Vector2 vel = bullet.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).state.vel;

			float offset = entity.getComponent(ComponentType.COLLISSION, CollissionComponent.class).state.collissionRadius;

			pos.setZero();
			pos.x = 3*offset;
			
			if (state.shootDir.len2() < 0.01) {
				pos.rotate(entity.angularTransform.state.ang);
			}else{
				pos.rotate(state.shootDir.angle());
			}
			vel.set(pos).nor().scl(state.bulletVel);

			pos.add(entity.linearTransform.state.pos);

			bullet.angularTransform.state.ang = vel.angle();
		}
	}

	public void destroy(GameEntity ent) {
	}

	@Override
	public Class<WeaponState> getStateClass() {
		return WeaponState.class;
	}

}
