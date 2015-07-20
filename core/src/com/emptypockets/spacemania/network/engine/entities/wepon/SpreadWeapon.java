package com.emptypockets.spacemania.network.engine.entities.wepon;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.client.input.ClientInput;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.entities.bullets.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.players.PlayerEntity;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

public class SpreadWeapon extends Weapon {
	long lastShootTime = 0;
	long shootInterval = 200;

	int bulletCount = 5;
	float spread = 30;

	public void shoot(ServerPlayer player, PlayerEntity entity, ServerEngine engine) {
		ClientInput input = player.getClientInput();
		if (input.getShoot().len2() < 0.1) {
			return;
		}
		if (System.currentTimeMillis() - lastShootTime > shootInterval) {
			lastShootTime = System.currentTimeMillis();

			Vector2 pos = entity.getPos().cpy();
			Vector2 direction = player.getClientInput().getShoot().cpy().nor();

			Vector2 offset = direction.cpy().scl(2 * entity.getRadius());

			offset.rotate(-spread / 2);
			for (int i = 0; i < bulletCount; i++) {
				BulletEntity bulletA = (BulletEntity) engine.getEntityManager().createEntity(EntityType.Bullet);
				bulletA.setPos(pos.x, pos.y);
				bulletA.getPos().add(offset);
				bulletA.getVel().set(offset).setLength(bulletA.getMaxVelocity());
				engine.getEntityManager().addEntity(bulletA);
				offset.rotate(spread / (bulletCount - 1));
			}
		}
	}
}
