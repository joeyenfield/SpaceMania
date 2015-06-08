package com.emptypockets.spacemania.network.engine.entities.behaviour;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.entities.PlayerEntity;

public class FollowEntity extends Behaviour {

	Entity selectedEntity;
	Vector2 desiredVelocity = new Vector2();
	Vector2 steering = new Vector2();
	
	public void update(Engine engine){
		if(selectedEntity == null || !selectedEntity.isAlive()){
			selectedEntity = (PlayerEntity) engine.getEntityManager().pickRandom(EntityType.Player);
		}		
	}
	
	
	@Override
	public void apply(Entity entity) {
		if(selectedEntity != null){
			desiredVelocity.set(selectedEntity.getPos()).sub(entity.getPos()).nor().scl(entity.getMaxVelocity());
			steering.set(desiredVelocity).sub(entity.getVel()).limit(entity.getMaxForce()).scl(entity.getInverseMass());
			entity.getVel().add(steering);
		}
	}

}
