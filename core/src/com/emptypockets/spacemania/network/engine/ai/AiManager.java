package com.emptypockets.spacemania.network.engine.ai;

import java.util.HashMap;

import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.EntityManagerInterface;
import com.emptypockets.spacemania.network.engine.entities.EnemyEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;

public class AiManager implements EntityManagerInterface {
	boolean enabled = false;

	HashMap<EnemyEntity, EntityAi> aiEntityList;

	public AiManager() {
		aiEntityList = new HashMap<EnemyEntity,EntityAi>();
	}

	@Override
	public synchronized void entityAdded(Entity entity) {
		if (enabled) {
			if (entity instanceof EnemyEntity) {
				EntityAi ai = new EntityAi(entity);
				aiEntityList.put((EnemyEntity) entity, ai);
			}
		}
	}

	public synchronized void update(Engine engine) {
		if (enabled) {
			for (EntityAi entityAi : aiEntityList.values()) {
				entityAi.update(engine);
			}
		}
	}

	@Override
	public synchronized void entityRemoved(Entity entity) {
		if (enabled) {
			aiEntityList.remove(entity);
		}
	}

	public synchronized void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
