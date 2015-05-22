package com.emptypockets.spacemania.network.engine.entities;

import com.emptypockets.spacemania.network.engine.EntityType;

public class PlayerEntity extends Entity {
	
	public PlayerEntity(){
		super(EntityType.Player);
	}
	
	@Override
	public boolean intersectsDetailed(Entity entity) {
		return false;
	}
}
