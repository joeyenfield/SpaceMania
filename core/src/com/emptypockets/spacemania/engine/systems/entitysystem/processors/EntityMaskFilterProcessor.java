package com.emptypockets.spacemania.engine.systems.entitysystem.processors;

import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityMaskFilterProcessor implements SingleProcessor<GameEntity>, Poolable {

	public Bits abilityMask;
	public SingleProcessor<GameEntity> processor;

	public void process(GameEntity entity) {
		if (entity.hasAnyOfAbility(abilityMask)) {
			processor.process(entity);
		}
	}

	@Override
	public void reset() {
		abilityMask = null;
		processor = null;
	}

}
