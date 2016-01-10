package com.emptypockets.spacemania.engine.systems.entitysystem.processors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntityUtils;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityRegionFilterProcessor implements SingleProcessor<GameEntity>, Poolable{
	public SingleProcessor<GameEntity> processor;
	public Bits abilityMask =null;
	public Rectangle region;

	@Override
	public void process(GameEntity entity) {
		if ((abilityMask == null || entity.hasAnyOfAbility(abilityMask)) && EntityUtils.isEntityInRegion(entity, region)) {
			processor.process(entity);
		}
	}

	@Override
	public void reset() {
		processor = null;
		abilityMask = null;
		region = null;
	}

}
