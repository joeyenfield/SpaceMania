package com.emptypockets.spacemania.network.old.engine.fsm;

import java.util.ArrayList;

public class StateMachine {
	ArrayList<StateMachineListener> listeners;

	State currentState;

	public StateMachine() {
		listeners = new ArrayList<StateMachineListener>();
	}

	public void notifyStateExit() {
		for (StateMachineListener listener : listeners) {
			listener.notifyStateExited(currentState);
		}
	}
	public void notifyStateEnter() {
		for (StateMachineListener listener : listeners) {
			listener.notifyStateEntered(currentState);
		}
	}

	public void changeState(State<?> nextState) {
		if (currentState != null) {
			currentState.exitState();
			notifyStateExit();
		}
		currentState = nextState;
		if (currentState != null) {
			currentState.enterState();
			notifyStateEnter();
		}
	}

	public void update() {
		if (currentState != null) {
			currentState.update();
		}
	}
}
