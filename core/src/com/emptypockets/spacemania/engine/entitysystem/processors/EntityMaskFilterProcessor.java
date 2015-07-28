package com.emptypockets.spacemania.engine.entitysystem.processors;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityMaskFilterProcessor implements SingleProcessor<GameEntity>, Poolable {

	public int abilityMask = 0;
	public SingleProcessor<GameEntity> processor;

	public void process(GameEntity entity) {
		if (entity.hasAbility(abilityMask)) {
			processor.process(entity);
		}
	}

	@Override
	public void reset() {
		abilityMask = 0;
		processor = null;
	}

}
