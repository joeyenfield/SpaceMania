package com.emptypockets.spacemania.network.engine.entities;


public class PlayerEntity extends Entity {
	
	public PlayerEntity(){
		super(EntityType.Player);
	}
	
	@Override
	public boolean intersectsDetailed(Entity entity) {
		return false;
	}
}
