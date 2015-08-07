package com.emptypockets.spacemania.network.old.engine.ai.steering;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.old.engine.Engine;
import com.emptypockets.spacemania.network.old.engine.entities.Entity;
import com.emptypockets.spacemania.network.old.engine.entities.MovingEntity;
import com.emptypockets.spacemania.network.old.engine.entities.enemy.EnemyEntity;

public class Separation extends Steering {

	float distance = 150;
	ArrayList<Entity> result = new ArrayList<Entity>();

	@Override
	public void updateSteeringForce(Engine engine, MovingEntity entity, Vector2 force) {
		maxForce = 1000;
		float x, y;
		float dst2;
		force.setZero();
		engine.getEntitySpatialPartition().getNearbyEntities(entity, distance, result);
		if (result.size() != 0) {
			engine.getEntitySpatialPartition().filterToType(result, EnemyEntity.class);
			if (result.size() != 0) {
				int size = result.size();
				for (int i = 0; i < size; i++) {
					Entity ent = result.get(i);
					dst2 = ent.getPos().dst2(entity.getPos());
					if (dst2 == 0) {
						// Add some randome movement to get stuff away from each
						// other
						x = MathUtils.random();
						y = MathUtils.random();
					} else {
						x = (entity.getPos().x - ent.getPos().x);
						y = (entity.getPos().y - ent.getPos().y);
					}
					force.x += x / (.1f + dst2);
					force.y += y / (.1f + dst2);
				}
				force.scl(maxForce);
			}
		}
		force.limit(maxForce);
		result.clear();
	}

}
