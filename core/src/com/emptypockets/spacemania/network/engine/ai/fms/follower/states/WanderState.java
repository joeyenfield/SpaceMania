package com.emptypockets.spacemania.network.engine.ai.fms.follower.states;

import com.emptypockets.spacemania.network.engine.ai.fms.follower.FollowerStateMachine;
import com.emptypockets.spacemania.network.engine.fsm.State;

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
