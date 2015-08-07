package com.emptypockets.spacemania.network.old.server.engine;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.old.engine.Engine;
import com.emptypockets.spacemania.network.old.engine.ai.manager.AiManager;
import com.emptypockets.spacemania.network.old.engine.entities.Entity;
import com.emptypockets.spacemania.network.old.engine.entities.EntityType;
import com.emptypockets.spacemania.network.old.engine.entities.bullets.BulletEntity;
import com.emptypockets.spacemania.network.old.engine.entities.collectable.CollectableEntity;
import com.emptypockets.spacemania.network.old.engine.entities.collectable.ScoreEntity;
import com.emptypockets.spacemania.network.old.engine.entities.enemy.EnemyEntity;
import com.emptypockets.spacemania.network.old.engine.entities.players.PlayerEntity;
import com.emptypockets.spacemania.network.old.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.old.server.player.ServerPlayerManager;

public class ServerEngine extends Engine {
	AiManager aiManager;
	ServerPlayerManager playerManager;

	long lastEnemy = 0;

	ArrayList<BulletEntity> tempBulletsHolder = new ArrayList<BulletEntity>();
	ArrayList<Entity> tempEntitiesHolder = new ArrayList<Entity>();

	public ServerEngine(ServerPlayerManager playerManager) {
		super();
		aiManager = new AiManager(this);
		aiManager.setEnabled(true);
		this.playerManager = playerManager;
		if (this.playerManager == null) {
			throw new RuntimeException("Should not be null");
		}
		getEntityManager().register(aiManager);
	}

	public void processCollissions() {

		// Process Bullets Hitting Enemys
		getEntityManager().filterEntities(BulletEntity.class, tempBulletsHolder);
		for (BulletEntity bullet : tempBulletsHolder) {
			tempEntitiesHolder.clear();
			getEntitySpatialPartition().getNearbyEntities(bullet, bullet.getLastMovementDist() * 2, tempEntitiesHolder, EnemyEntity.class);
			ENEMY_LOOP: for (Entity ent : tempEntitiesHolder) {
				EnemyEntity enemy = (EnemyEntity) ent;
				if (bullet.contact(enemy)) {
					bullet.setAlive(false);
					bullet.setExplodes(true);
					enemy.setAlive(false);
					enemy.setExplodes(true);
					for (int i = 0; i < 2; i++) {
						ScoreEntity score = (ScoreEntity) getEntityManager().createEntity(EntityType.Score);
						score.setPos(bullet.getPos());
						score.getPos().add(MathUtils.random(-20, 20), MathUtils.random(-20, 20));
						float scoreVel = score.getLaunchVel();
						score.getVel().set(MathUtils.random(-scoreVel, scoreVel), MathUtils.random(-scoreVel, scoreVel));
						getEntityManager().addEntity(score);
					}
					break ENEMY_LOOP;
				}
			}
		}
		tempBulletsHolder.clear();
		tempEntitiesHolder.clear();

		// Process Players
		final Vector2 force = new Vector2();
		playerManager.process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer serverPlayer) {
				int id = serverPlayer.getEntityId();
				PlayerEntity playerEnt = (PlayerEntity) getEntityManager().getEntityById(id);
				if (playerEnt != null) {

					// Check Enemy Collissions
					tempEntitiesHolder.clear();
					getEntitySpatialPartition().getNearbyEntities(playerEnt, playerEnt.getRadius(), tempEntitiesHolder, EnemyEntity.class);
					for (Entity enemyEntity : tempEntitiesHolder) {
						EnemyEntity enemy = (EnemyEntity) enemyEntity;
						if (enemy.contact(playerEnt)) {
							serverPlayer.attacked(enemy, playerEnt);
						}
					}

					// Check Collectables
					tempEntitiesHolder.clear();
					getEntitySpatialPartition().getNearbyEntities(playerEnt, serverPlayer.getMagnetDistance(), tempEntitiesHolder, CollectableEntity.class);
					for (Entity col : tempEntitiesHolder) {
						CollectableEntity collect = (CollectableEntity) col;
						if (collect.contact(playerEnt)) {
							serverPlayer.collect(collect, playerEnt);
						} else {
							force.set(playerEnt.getPos()).sub(collect.getPos()).nor().setLength(collect.getMaxForce());
							collect.applyForce(force);
						}
					}

				}
			}
		});
		tempEntitiesHolder.clear();
	}

	public void updateAi() {
		aiManager.update(this);
	}

	public void removeDeadEntities() {
		getEntityManager().removeDead();
	}

	public void spawnEnemy() {
		if (!Constants.ENTITY_SPAWN)
			return;
		if (System.currentTimeMillis() - lastEnemy > Constants.ENTITY_SPAWN_TIME) {
			lastEnemy = System.currentTimeMillis();
			if (getEntityManager().countType(EntityType.Enemy_FOLLOW) < Constants.ENTITY_SPAWN_FOLLOW_COUNT) {
				EnemyEntity entity = (EnemyEntity) getEntityManager().createEntity(EntityType.Enemy_FOLLOW);
				entity.setPos(getRegion().x + getRegion().width * MathUtils.random(), getRegion().y + getRegion().height * MathUtils.random());
				moveToDistantRegionWithoutEntities(Constants.ENTITY_SPAWN_PLAYER_DISTANCE, entity, PlayerEntity.class);
				getEntityManager().addEntity(entity);
			}
			if (getEntityManager().countType(EntityType.Enemy_RANDOM) < Constants.ENTITY_SPAWN_RANDOM_COUNT) {
				EnemyEntity entity = (EnemyEntity) getEntityManager().createEntity(EntityType.Enemy_RANDOM);
				entity.setPos(getRegion().x + getRegion().width * MathUtils.random(), getRegion().y + getRegion().height * MathUtils.random());
				moveToDistantRegionWithoutEntities(Constants.ENTITY_SPAWN_PLAYER_DISTANCE, entity,PlayerEntity.class);
				getEntityManager().addEntity(entity);
			}
		}
	}

	public void moveToDistantRegionWithoutEntities(float dist, Entity entity, Class<?> entityTypes) {
		// times = 10;
		if (!getEntitySpatialPartition().hasNearbyEntities(entity, dist,entityTypes)) {
			return;
		}
		for (int i = 0; i < 20; i++) {
			entity.setPos(getRegion().x + getRegion().width * MathUtils.random(), getRegion().y + getRegion().height * MathUtils.random());
			if (!getEntitySpatialPartition().hasNearbyEntities(entity, dist, entityTypes)) {
				return;
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
		logEntity("server");
	}

	public ServerPlayerManager getPlayerManager() {
		return playerManager;
	}
}
