package com.emptypockets.spacemania.engine.systems.entitysystem.processors;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityTypeCountProcessor implements SingleProcessor<GameEntity>, Poolable{
	public int count = 0;
	public GameEntityType type;

	@Override
	public void process(GameEntity entity) {
		if(entity.type == type){
			count++;
		}
	}

	@Override
	public void reset() {
		type = null;
		count = 0;
	}
}
