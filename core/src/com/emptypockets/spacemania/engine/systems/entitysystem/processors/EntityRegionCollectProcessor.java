package com.emptypockets.spacemania.engine.systems.entitysystem.processors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntityUtils;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityRegionCollectProcessor implements SingleProcessor<GameEntity>, Poolable {


	public ArrayListProcessor<GameEntity> entities;
	public Bits abilityMask;
	public Rectangle region;

	@Override
	public void process(GameEntity entity) {
		if ((abilityMask == null || entity.hasAnyOfAbility(abilityMask)) && EntityUtils.isEntityInRegion(entity, region)) {
			entities.add(entity);
		}
	}

	@Override
	public void reset() {
		entities = null;
		abilityMask = null;
		region = null;		
	}

}
