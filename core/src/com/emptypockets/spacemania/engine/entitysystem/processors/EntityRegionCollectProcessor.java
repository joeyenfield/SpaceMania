package com.emptypockets.spacemania.engine.entitysystem.processors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.entitysystem.EntityUtils;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityRegionCollectProcessor implements SingleProcessor<GameEntity>, Poolable {


	public ArrayListProcessor<GameEntity> entities;
	public int abilityMask = 0;
	public Rectangle region;

	@Override
	public void process(GameEntity entity) {
		if ((abilityMask == 0 || entity.hasAbility(abilityMask)) && EntityUtils.isEntityInRegion(entity, region)) {
			entities.add(entity);
		}
	}

	@Override
	public void reset() {
		entities = null;
		abilityMask = 0;
		region = null;		
	}

}
