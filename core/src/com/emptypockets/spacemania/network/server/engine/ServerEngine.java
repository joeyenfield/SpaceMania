package com.emptypockets.spacemania.network.server.engine;

import java.util.ArrayList;

import javax.management.RuntimeErrorException;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.Constants;
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
import com.emptypockets.spacemania.network.server.player.ServerPlayerManager;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;

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
		if(this.playerManager == null){
			throw new RuntimeException("Should not be null");
		}
		getEntityManager().register(aiManager);
	}

	public void processCollissions() {
		 getEntityManager().filterEntities(BulletEntity.class,tempBulletsHolder);

		// Kill Enemys
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

		// Get Collectable
		final Vector2 force = new Vector2();
		playerManager.process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer player) {
				int id = player.getEntityId();
				Entity ent = getEntityManager().getEntityById(id);
				if (ent != null) {
					tempEntitiesHolder.clear();
					PlayerEntity playerEntity = (PlayerEntity) ent;
					getEntitySpatialPartition().getNearbyEntities(ent, playerEntity.getMagnetDistance(), tempEntitiesHolder, CollectableEntity.class);
					for (Entity col : tempEntitiesHolder) {
						CollectableEntity collect = (CollectableEntity) col;
						if (collect.contact(playerEntity)) {
							collect.collect(player);
						} else {
							force.set(playerEntity.getPos()).sub(col.getPos()).nor().setLength(col.getMaxForce());
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
			if (getEntityManager().countType(EntityType.Enemy_FOLLOW) <  Constants.ENTITY_SPAWN_FOLLOW_COUNT) {
				EnemyEntity entity = (EnemyEntity) getEntityManager().createEntity(EntityType.Enemy_FOLLOW);
				entity.setPos(getRegion().x + getRegion().width * MathUtils.random(), getRegion().y + getRegion().height * MathUtils.random());
				getEntityManager().addEntity(entity);
			}
			if (getEntityManager().countType(EntityType.Enemy_RANDOM) <  Constants.ENTITY_SPAWN_RANDOM_COUNT) {
				EnemyEntity entity = (EnemyEntity) getEntityManager().createEntity(EntityType.Enemy_RANDOM);
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
		logEntity("server");
	}

	public ServerPlayerManager getPlayerManager() {
		return playerManager;
	}
}
