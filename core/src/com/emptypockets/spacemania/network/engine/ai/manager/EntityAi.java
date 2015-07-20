package com.emptypockets.spacemania.network.engine.ai.manager;

import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.MovingEntity;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;

public abstract class EntityAi {
	MovingEntity entity;
	ServerEngine engine;

	public EntityAi(ServerEngine engine, MovingEntity entity) {
		super();
		this.engine = engine;
		this.entity = entity;
		setupSteering();
	}

	public abstract void setupSteering();

	public abstract void update();

	public abstract void apply();

	public MovingEntity getEntity() {
		return entity;
	}

	public ServerEngine getEngine() {
		return engine;
	}

}
