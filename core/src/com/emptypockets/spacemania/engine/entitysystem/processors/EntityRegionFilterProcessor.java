package com.emptypockets.spacemania.engine.entitysystem.processors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.entitysystem.EntityUtils;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityRegionFilterProcessor implements SingleProcessor<GameEntity>, Poolable{
	public SingleProcessor<GameEntity> processor;
	public int abilityMask = 0;
	public Rectangle region;

	@Override
	public void process(GameEntity entity) {
		if ((abilityMask == 0 || entity.hasAbility(abilityMask)) && EntityUtils.isEntityInRegion(entity, region)) {
			processor.process(entity);
		}
	}

	@Override
	public void reset() {
		processor = null;
		abilityMask = 0;
		region = null;
	}

}
