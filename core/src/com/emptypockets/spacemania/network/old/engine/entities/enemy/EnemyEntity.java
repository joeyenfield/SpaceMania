package com.emptypockets.spacemania.network.old.engine.entities.enemy;

import com.badlogic.gdx.graphics.Color;
import com.emptypockets.spacemania.network.old.engine.entities.EntityType;
import com.emptypockets.spacemania.network.old.engine.entities.MovingEntity;


public class EnemyEntity extends MovingEntity{
	public EnemyEntity() {
		super(EntityType.Enemy_RANDOM);
		setColor(Color.RED);
		setRadius(20);
		setMaxVelocity(200);
		setMaxForce(1000);
	}
}
