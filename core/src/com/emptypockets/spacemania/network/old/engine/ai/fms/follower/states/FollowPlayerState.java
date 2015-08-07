package com.emptypockets.spacemania.network.old.engine.ai.fms.follower.states;

import com.emptypockets.spacemania.network.old.engine.ai.fms.follower.FollowerStateMachine;
import com.emptypockets.spacemania.network.old.engine.ai.steering.Follow;
import com.emptypockets.spacemania.network.old.engine.entities.EntityType;
import com.emptypockets.spacemania.network.old.engine.entities.players.PlayerEntity;
import com.emptypockets.spacemania.network.old.engine.fsm.State;

public class FollowPlayerState extends State<FollowerStateMachine> {

	Follow follow;
	PlayerEntity player;

	public FollowPlayerState(FollowerStateMachine stateMachine) {
		super(stateMachine);
		follow = new Follow();
	}

	@Override
	public void enterState() {
		if (player == null || !player.isAlive()) {
			player = (PlayerEntity) getStateMachine().getEngine().getEntityManager().pickRandom(EntityType.Player);
			follow.setTarget(player);
		}
	}

	@Override
	public void exitState() {

	}

	public boolean checkBullet(){
		return false;
	}
	@Override
	public void update() {
		if (checkBullet()) {
			getStateMachine().fleeBullets();
		} else if (player == null || !player.isAlive()) {
			getStateMachine().wander();
		} else {
			follow.update(getStateMachine().getEngine(), getStateMachine().getEntity());
			getStateMachine().getEntity().applyForce(follow.getSteeringForce());
		}
	}

}
