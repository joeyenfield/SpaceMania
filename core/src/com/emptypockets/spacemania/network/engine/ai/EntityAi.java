package com.emptypockets.spacemania.network.engine.ai;

import com.badlogic.gdx.graphics.Color;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.behaviour.Behaviour;
import com.emptypockets.spacemania.network.engine.entities.behaviour.FollowEntity;

public class EntityAi {
	Entity entity;
	Behaviour behaviour;
	
	public EntityAi(Entity entity) {
		super();
		this.entity = entity;
		switch (entity.getType()) {
		case Enemy_FOLLOW:
			behaviour= new FollowEntity();
			break;
		case Enemy_RANDOM:
			behaviour= new FollowEntity();
			break;
		default:
			throw new RuntimeException("Unknown Enemy");
		}
		behaviour = new FollowEntity();
	}

	public void update(Engine engine){
		behaviour.update(engine);
		behaviour.apply(entity);
	}
}
