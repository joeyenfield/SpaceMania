package com.emptypockets.spacemania.network.old.engine.fsm;

public interface StateMachineListener {
	public void notifyStateEntered(State state);

	public void notifyStateExited(State state);

}
