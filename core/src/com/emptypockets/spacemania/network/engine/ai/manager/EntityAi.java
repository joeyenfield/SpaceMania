package com.emptypockets.spacemania.network.engine.ai.manager;

import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;

public abstract class EntityAi {
	Entity entity;
	ServerEngine engine;

	public EntityAi(ServerEngine engine, Entity entity) {
		super();
		this.engine = engine;
		this.entity = entity;
		setupSteering();
	}

	public abstract void setupSteering();

	public abstract void update();

	public abstract void apply();

	public Entity getEntity() {
		return entity;
	}

	public ServerEngine getEngine() {
		return engine;
	}

}
