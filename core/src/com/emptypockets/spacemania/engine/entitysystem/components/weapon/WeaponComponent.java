package com.emptypockets.spacemania.engine.entitysystem.components.weapon;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.collission.CollissionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.destruction.DestructionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementData;
import com.emptypockets.spacemania.engine.entitysystem.components.partition.PartitionComponent;
import com.emptypockets.spacemania.utils.PoolsManager;

public class WeaponComponent extends EntityComponent<WeaponData> {

	long lastShoot = 0;

	public WeaponComponent() {
		super(ComponentType.WEAPON);
	}

	public void update(float deltaTime) {
		if (System.currentTimeMillis() - lastShoot > data.shootTime && data.shooting) {
			lastShoot = System.currentTimeMillis();

			GameEntity bullet = entity.engine.entityFactory.createBulletEntity(data.bulletLife);

			Vector2 pos = bullet.linearTransform.data.pos;
			Vector2 vel = bullet.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).data.vel;

			pos.setZero();
			pos.x = entity.getComponent(ComponentType.COLLISSION, CollissionComponent.class).data.collissionRadius;
			pos.x += 2 * bullet.getComponent(ComponentType.COLLISSION, CollissionComponent.class).data.collissionRadius;
			pos.rotate(entity.angularTransform.data.ang);
			vel.set(pos).nor().scl(data.bulletVel);
			pos.add(entity.linearTransform.data.pos);

			bullet.angularTransform.data.ang = vel.angle();

			entity.engine.addEntity(bullet);
		}
	}

	public void destroy(GameEntity ent) {
	}

	@Override
	public Class<WeaponData> getDataClass() {
		return WeaponData.class;
	}

}
