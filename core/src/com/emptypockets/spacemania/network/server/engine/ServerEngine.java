package com.emptypockets.spacemania.network.server.engine;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.math.MathUtils;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.ai.AiManager;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.EnemyEntity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;

public class ServerEngine extends Engine {
	AiManager aiManager;
	long lastEnemy = 0;
	
	public ServerEngine() {
		super();
		aiManager = new AiManager();
		aiManager.setEnabled(true);
		getEntityManager().register(aiManager);
	}

	public void processCollissions() {
		ArrayList<BulletEntity> bullets = getEntityManager().filterEntities(BulletEntity.class);
		ArrayList<EnemyEntity> enemys = getEntityManager().filterEntities(EnemyEntity.class);
		
		//Kill Enemys
		for(BulletEntity bullet : bullets){
			ENEMY_LOOP : for(EnemyEntity enemy : enemys){
				if(bullet.contact(enemy)){
					bullet.setAlive(false);
					enemy.setAlive(false);
					break ENEMY_LOOP;
				}
			}
		}
	}

	public void updateAi() {
		aiManager.update(this);
	}

	public void removeDeadEntities() {
		getEntityManager().removeDead();
	}
	
	public void spawnEnemy(){
		if(System.currentTimeMillis()-lastEnemy > 1000 && getEntityManager().countType(EntityType.Enemy_FOLLOW) < 30){
			lastEnemy = System.currentTimeMillis();
			EnemyEntity entity = (EnemyEntity) getEntityManager().createEntity(EntityType.Enemy_FOLLOW);
			
			entity.setPos(getRegion().x+getRegion().width*MathUtils.random(), getRegion().y+getRegion().height*MathUtils.random());
			getEntityManager().addEntity(entity);
		}
	}

	@Override
	public void update() {
		processCollissions();
		removeDeadEntities();
//		spawnEnemy();
//		updateAi();
		super.update();
	}
}
