package com.emptypockets.spacemania.network.engine.ai.steering;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.MovingEntity;
import com.emptypockets.spacemania.network.engine.entities.bullets.BulletEntity;

public class Separation extends Steering {

	float distance = 150;
	ArrayList<Entity> result = new ArrayList<Entity>();

	@Override
	public void updateSteeringForce(Engine engine, MovingEntity entity, Vector2 force) {
		force.setZero();
		engine.getEntitySpatialPartition().getNearbyEntities(entity, distance, result);
		if (result.size() != 0) {
			engine.getEntitySpatialPartition().filterRemove(result, BulletEntity.class);
			int size = result.size();
			for(int i = 0; i < size; i++){
				Entity ent = result.get(i);
				force.x = (ent.getPos().x-entity.getPos().x);
				force.y = (ent.getPos().y-entity.getPos().y);
			}
			force.nor();
		}
		result.clear();
		
	}

}
