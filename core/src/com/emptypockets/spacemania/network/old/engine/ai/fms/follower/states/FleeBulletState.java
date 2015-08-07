package com.emptypockets.spacemania.network.old.engine.ai.fms.follower.states;

import com.emptypockets.spacemania.network.old.engine.ai.fms.EntityStateMachine;
import com.emptypockets.spacemania.network.old.engine.ai.steering.Flee;
import com.emptypockets.spacemania.network.old.engine.entities.EntityType;
import com.emptypockets.spacemania.network.old.engine.entities.bullets.BulletEntity;
import com.emptypockets.spacemania.network.old.engine.fsm.State;

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
