package com.emptypockets.spacemania.network.server.engine;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.ai.manager.AiManager;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.EnemyEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;
import com.emptypockets.spacemania.network.engine.entities.collect.CollectableEntity;
import com.emptypockets.spacemania.network.engine.entities.collect.ScoreEntity;
import com.emptypockets.spacemania.network.server.player.PlayerManager;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

public class ServerEngine extends Engine {
	AiManager aiManager;
	PlayerManager playerManager;

	long lastEnemy = 0;

	public ServerEngine(PlayerManager playerManager) {
		super();
		aiManager = new AiManager(this);
		aiManager.setEnabled(true);
		this.playerManager = playerManager;
		getEntityManager().register(aiManager);
	}

	public void processCollissions() {
		ArrayList<BulletEntity> bullets = getEntityManager().filterEntities(BulletEntity.class);

		final ArrayList<Entity> enemys = new ArrayList<Entity>();
		// Kill Enemys
		for (BulletEntity bullet : bullets) {
			enemys.clear();
			getEntitySpatialPartition().getNearbyEntities(bullet, bullet.getLastMovementDist() * 2, enemys, EnemyEntity.class);
			ENEMY_LOOP: for (Entity ent : enemys) {
				EnemyEntity enemy = (EnemyEntity) ent;
				if (bullet.contact(enemy)) {
					bullet.setAlive(false);
					enemy.setAlive(false);
					ScoreEntity score = (ScoreEntity) getEntityManager().createEntity(EntityType.Score);
					score.setPos(bullet.getPos());
					score.getVel().set(MathUtils.random(-score.getMaxVelocity(), score.getMaxVelocity()), MathUtils.random(-score.getMaxVelocity(), score.getMaxVelocity()));
					getEntityManager().addEntity(score);
					break ENEMY_LOOP;
				}
			}
		}
		enemys.clear();

		final ArrayList<Entity> collectable = new ArrayList<Entity>();
		// Get Collectable
		playerManager.process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer player) {
				int id = player.getEntityId();
				Entity ent = getEntityManager().getEntityById(id);
				if (ent != null) {
					collectable.clear();
					getEntitySpatialPartition().getNearbyEntities(ent, ent.getLastMovementDist(), collectable, CollectableEntity.class);
					PlayerEntity playerEntity = (PlayerEntity) ent;
					for (Entity col : collectable) {
						CollectableEntity collect = (CollectableEntity) col;
						if (collect.contact(playerEntity)) {
							collect.collect(player);
						}
					}

				}
			}
		});

	}

	public void updateAi() {
		aiManager.update(this);
	}

	public void removeDeadEntities() {
		getEntityManager().removeDead();
	}

	public void spawnEnemy() {
		if (System.currentTimeMillis() - lastEnemy > 500 && getEntityManager().countType(EntityType.Enemy_FOLLOW) < 50) {
			lastEnemy = System.currentTimeMillis();
			for (int i = 0; i < 10; i++) {
				EnemyEntity entity = (EnemyEntity) getEntityManager().createEntity(EntityType.Enemy_FOLLOW);

				entity.setPos(getRegion().x + getRegion().width * MathUtils.random(), getRegion().y + getRegion().height * MathUtils.random());
				getEntityManager().addEntity(entity);
			}
		}
	}

	@Override
	public void update() {
		processCollissions();
		removeDeadEntities();
		spawnEnemy();
		updateAi();
		super.update();
	}
}
