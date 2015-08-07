package com.emptypockets.spacemania.network.old.engine.ai.fms.follower.states;

import com.emptypockets.spacemania.network.old.engine.ai.fms.follower.FollowerStateMachine;
import com.emptypockets.spacemania.network.old.engine.fsm.State;

public class WanderState extends State<FollowerStateMachine>{

	public WanderState(FollowerStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public void enterState() {
		
	}

	@Override
	public void exitState() {
		
	}

	@Override
	public void update() {
		getStateMachine().followPlayer();
	}

}
