package com.emptypockets.spacemania.network.engine.fsm;

public interface StateMachineListener {
	public void notifyStateEntered(State state);

	public void notifyStateExited(State state);

}
