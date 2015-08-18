package com.emptypockets.spacemania.engine.systems.entitysystem;

import com.emptypockets.spacemania.holders.SingleProcessor;

public abstract class EntitySystemManager{

	public EntitySystemManager() {
		super();
	}

	public abstract void manage(EntitySystem entitySystem, float deltaTime);
	
}