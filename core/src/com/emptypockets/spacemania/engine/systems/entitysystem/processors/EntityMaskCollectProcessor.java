package com.emptypockets.spacemania.engine.systems.entitysystem.processors;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityMaskCollectProcessor implements SingleProcessor<GameEntity>, Poolable {

	public ArrayListProcessor<GameEntity> entities;
	public Bits abilityMask;

	@Override
	public void process(GameEntity entity) {
		if (entity.hasAnyOfAbility(abilityMask)) {
			entities.add(entity);
		}
	}

	@Override
	public void reset() {
		abilityMask = null;
		entities = null;
	}

}
