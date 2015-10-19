package com.emptypockets.spacemania.engine.systems.entitysystem.processors;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityMaskArrayListProcessor implements SingleProcessor<GameEntity>, Poolable {

	public ArrayList<GameEntity> entities;
	public int abilityMask;

	@Override
	public void process(GameEntity entity) {
		if (entity.hasAnyOfAbility(abilityMask)) {
			entities.add(entity);
		}
	}

	@Override
	public void reset() {
		abilityMask = 0;
		entities = null;
	}

}
