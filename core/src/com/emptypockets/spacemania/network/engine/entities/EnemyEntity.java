package com.emptypockets.spacemania.network.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.behaviour.FollowEntity;


public class EnemyEntity extends Entity{
	public EnemyEntity() {
		super(EntityType.Enemy_RANDOM);
		setColor(Color.RED);
	}
	
	
}
