package com.emptypockets.spacemania.network.old.engine.ai.fms;

import com.emptypockets.spacemania.network.old.engine.entities.Entity;
import com.emptypockets.spacemania.network.old.engine.fsm.StateMachine;
import com.emptypockets.spacemania.network.old.server.engine.ServerEngine;

public class EntityStateMachine<ENT extends Entity> extends StateMachine {
	ENT entity;
	ServerEngine engine;

	public EntityStateMachine(ENT entity, ServerEngine engine) {
		super();
		this.entity = entity;
		this.engine = engine;
	}

	public ENT getEntity() {
		return entity;
	}

	public void setEntity(ENT entity) {
		this.entity = entity;
	}

	public ServerEngine getEngine() {
		return engine;
	}

	public void setEngine(ServerEngine engine) {
		this.engine = engine;
	}
	

	public void defaultState(){
		changeState(null);
	}

}
