package com.emptypockets.spacemania.network.engine.ai.fms.follower.states;

import com.emptypockets.spacemania.network.engine.ai.fms.EntityStateMachine;
import com.emptypockets.spacemania.network.engine.ai.fms.follower.FollowerStateMachine;
import com.emptypockets.spacemania.network.engine.ai.steering.Flee;
import com.emptypockets.spacemania.network.engine.entities.BulletEntity;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.fsm.State;

public class FleeBulletState<EMS extends EntityStateMachine> extends State<EMS>{

	BulletEntity entity;
	Flee flee;
	public FleeBulletState(EMS stateMachine) {
		super(stateMachine);
		flee = new Flee();
	}

	public void updateDangerousBullet(){
		entity = (BulletEntity) getStateMachine().getEngine().getEntitySpatialPartition().searchNearestEntityWhereEntityInThereFOV(getStateMachine().getEntity(), EntityType.Bullet, 600, 1);
	}

	public boolean hasDangerousBullet(){
		return entity == null;
	}
	@Override
	public void enterState() {

	}

	@Override
	public void exitState() {
		entity = null;
	}

	@Override
	public void update() {
		if(hasDangerousBullet()){
			
		}else{
			getStateMachine().defaultState();
		}
	}

}
