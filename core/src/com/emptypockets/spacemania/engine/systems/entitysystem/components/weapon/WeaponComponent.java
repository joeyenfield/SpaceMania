package com.emptypockets.spacemania.engine.systems.entitysystem.components.weapon;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.collission.CollissionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction.DestructionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;

public class WeaponComponent extends EntityComponent<WeaponData> {

	long lastShoot = 0;

	public WeaponComponent() {
		super(ComponentType.WEAPON);
	}

	public void update(float deltaTime) {
		if (System.currentTimeMillis() - lastShoot > data.shootTime && data.shooting) {
			lastShoot = System.currentTimeMillis();

			GameEntity bullet = entity.engine.createEntity(GameEntityType.BULLET);
			WeaponComponent weaponComp = entity.getComponent(ComponentType.WEAPON, WeaponComponent.class);
			DestructionComponent desComp = bullet.getComponent(ComponentType.DESTRUCTION, DestructionComponent.class);
			if (desComp != null) {
				desComp.data.destroyTime = entity.engine.getTime() + (weaponComp.data.bulletLife);
			}
			Vector2 pos = bullet.linearTransform.data.pos;
			Vector2 vel = bullet.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).data.vel;

			pos.setZero();
			pos.x = entity.getComponent(ComponentType.COLLISSION, CollissionComponent.class).data.collissionRadius;
			pos.x += 5 * bullet.getComponent(ComponentType.COLLISSION, CollissionComponent.class).data.collissionRadius;
			pos.rotate(entity.angularTransform.data.ang);
			vel.set(pos).nor().scl(data.bulletVel);
			pos.add(entity.linearTransform.data.pos);

			bullet.angularTransform.data.ang = vel.angle();
		}
	}

	public void destroy(GameEntity ent) {
	}

	@Override
	public Class<WeaponData> getDataClass() {
		return WeaponData.class;
	}

}
