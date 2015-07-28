package com.emptypockets.spacemania.engine.entitysystem.processors;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityMaskCollectProcessor implements SingleProcessor<GameEntity>, Poolable {

	public ArrayListProcessor<GameEntity> entities;
	public int abilityMask;

	@Override
	public void process(GameEntity entity) {
		if (entity.hasAbility(abilityMask)) {
			entities.add(entity);
		}
	}

	@Override
	public void reset() {
		abilityMask = 0;
		entities = null;
	}

}
