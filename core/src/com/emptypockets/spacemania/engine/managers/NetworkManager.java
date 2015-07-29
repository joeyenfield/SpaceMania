package com.emptypockets.spacemania.engine.managers;

import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystemManager;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class NetworkManager extends EntitySystemManager implements SingleProcessor<GameEntity>{

	@Override
	public void manage(EntitySystem entitySystem, float deltaTime) {

	}

	@Override
	public void process(GameEntity entity) {
		
	}
}
