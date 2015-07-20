package com.emptypockets.spacemania.network.engine.entities;

import com.emptypockets.spacemania.network.engine.entities.states.EntityState;

public class StaticEntity extends Entity<EntityState>{

	public StaticEntity(EntityType type) {
		super(type, new EntityState());
	}

}
