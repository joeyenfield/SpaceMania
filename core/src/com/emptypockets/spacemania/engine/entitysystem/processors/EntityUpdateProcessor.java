package com.emptypockets.spacemania.engine.entitysystem.processors;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityUpdateProcessor implements SingleProcessor<GameEntity> {

	public float updateTime;
	@Override
	public void process(GameEntity entity) {
		entity.update(updateTime);
	}

}
