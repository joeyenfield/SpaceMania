package com.emptypockets.spacemania.engine.entitysystem.processors.filter;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEntityFilterProcessor implements SingleProcessor<GameEntity>, Poolable {
	ArrayList<GameEntity> entities = new ArrayList<GameEntity>();

	public GameEntityFilter filter;

	@Override
	public void process(GameEntity entity) {
		if (filter.accept(entity)) {
			entities.add(entity);
		}
	}

	public GameEntity pickRandom() {
		if (entities.size() > 0) {
			return entities.get(MathUtils.random(entities.size() - 1));
		}
		return null;
	}

	public void filter(EntitySystem entitySystem) {
		entitySystem.process(this);
	}

	@Override
	public void reset() {
		entities.clear();
		PoolsManager.free(filter);
	}
}
