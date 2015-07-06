package com.emptypockets.spacemania.network.engine.entities.wepon;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.client.input.ClientInput;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

public class BasicWeapon extends Weapon {
	long lastShootTime = 0;
	long shootInterval = 150;

	public void shoot(ServerPlayer player, PlayerEntity entity, ServerEngine engine) {
		ClientInput input = player.getClientInput();
		if (input.getShoot().len2() < 0.1) {
			return;
		}
		if (System.currentTimeMillis() - lastShootTime > shootInterval) {
			lastShootTime = System.currentTimeMillis();

			Vector2 pos = entity.getPos().cpy();
			Vector2 dir = player.getClientInput().getShoot().cpy().nor();

			pos.add(dir.cpy().nor().scl(entity.getRadius()));
			BulletEntity bulletA = (BulletEntity) engine.getEntityManager().createEntity(EntityType.Bullet);
			bulletA.setPos(pos.x, pos.y);
			bulletA.getVel().set(dir).scl(bulletA.getMaxVelocity());
			bulletA.setOwner(entity);
			engine.getEntityManager().addEntity(bulletA);
		}
	}

}
