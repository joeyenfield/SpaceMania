package com.emptypockets.spacemania.network.engine.ai.manager;

import java.util.HashMap;

import com.badlogic.gdx.math.MathUtils;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.EntityManagerInterface;
import com.emptypockets.spacemania.network.engine.entities.EnemyEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;

public class AiManager implements EntityManagerInterface {
	boolean enabled = false;

	HashMap<EnemyEntity, EntityAi> aiEntityList;
	ServerEngine engine;

	public AiManager(ServerEngine engine) {
		aiEntityList = new HashMap<EnemyEntity, EntityAi>();
		this.engine = engine;
	}

	@Override
	public synchronized void entityAdded(Entity entity) {
		if (enabled) {
			if (entity instanceof EnemyEntity) {
				EntityAi ai;
				switch (entity.getType()) {
				case Enemy_FOLLOW:
					ai = new FollowerEntityAi(engine, entity);
					entity.getState().setAng(MathUtils.random(360));
					entity.getState().setAngVel(360);
					break;
				case Enemy_RANDOM:
					ai = new RandomEntityAi(engine, entity);
					entity.getState().setAng(MathUtils.random(360));
					entity.getState().setAngVel(360);
					break;
				default:
					throw new RuntimeException("Unknown Enemy");
				}
				aiEntityList.put((EnemyEntity) entity, ai);
			}
		}
	}

	public synchronized void update(Engine engine) {
		if (enabled) {
			for (EntityAi entityAi : aiEntityList.values()) {
				entityAi.update();
				entityAi.apply();
			}
		}
	}

	@Override
	public synchronized void entityRemoved(Entity entity, boolean killed) {
		if (enabled) {
			aiEntityList.remove(entity);
		}
	}

	public synchronized void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
