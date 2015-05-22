package com.emptypockets.spacemania.network.engine.entities;

import com.emptypockets.spacemania.network.engine.EntityType;

public class EnemyEntity extends Entity{

	public EnemyEntity() {
		super(EntityType.Enemy);
	}
	
	@Override
	public boolean intersectsDetailed(Entity entity) {
		return false;
	}
}
