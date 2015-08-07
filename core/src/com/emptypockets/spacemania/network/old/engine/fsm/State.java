package com.emptypockets.spacemania.network.old.engine.fsm;

public abstract class State<SM extends StateMachine> {
	SM stateMachine;

	public State(SM stateMachine) {
		this.stateMachine = stateMachine;
	}

	public abstract void enterState();

	public abstract void exitState();

	public abstract void update();

	public SM getStateMachine() {
		return stateMachine;
	}

}
