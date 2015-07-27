package com.emptypockets.spacemania.engine.entitysystem.processors;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class EntityMaskFilterProcessor implements SingleProcessor<GameEntity> {

	public ArrayList<GameEntity> entities;
	public int abilityMask;

	@Override
	public void process(GameEntity entity) {
		if (entity.hasAbility(abilityMask)) {
			entities.add(entity);
		}
	}

}
