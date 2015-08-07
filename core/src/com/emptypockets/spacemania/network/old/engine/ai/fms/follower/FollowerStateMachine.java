package com.emptypockets.spacemania.network.old.engine.ai.fms.follower;

import com.emptypockets.spacemania.network.old.engine.ai.fms.EntityStateMachine;
import com.emptypockets.spacemania.network.old.engine.ai.fms.follower.states.FleeBulletState;
import com.emptypockets.spacemania.network.old.engine.ai.fms.follower.states.FollowPlayerState;
import com.emptypockets.spacemania.network.old.engine.ai.fms.follower.states.WanderState;
import com.emptypockets.spacemania.network.old.engine.entities.EntityType;
import com.emptypockets.spacemania.network.old.engine.entities.enemy.EnemyEntity;
import com.emptypockets.spacemania.network.old.server.engine.ServerEngine;

public class FollowerStateMachine extends EntityStateMachine<EnemyEntity> {
	FollowPlayerState followPlayerState;
	FleeBulletState fleeBulletState;
	WanderState wanderState;

	public FollowerStateMachine(EnemyEntity entity, ServerEngine engine) {
		super(entity, engine);
		if (entity.getType() != EntityType.Enemy_FOLLOW) {
			throw new RuntimeException("Invalid Entity Type");
		}
		followPlayerState = new FollowPlayerState(this);
		fleeBulletState = new FleeBulletState(this);
		wanderState = new WanderState(this);

		wander();
	}

	public FollowPlayerState getFollowPlayerState() {
		return followPlayerState;
	}

	public FleeBulletState getFleeBulletState() {
		return fleeBulletState;
	}

	public WanderState getWanderState() {
		return wanderState;
	}

	public void wander() {
		changeState(wanderState);
	}

	public void followPlayer() {
		changeState(followPlayerState);
	}

	public void fleeBullets() {
		changeState(fleeBulletState);
	}

}
