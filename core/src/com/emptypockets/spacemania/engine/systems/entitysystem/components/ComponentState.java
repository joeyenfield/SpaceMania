package com.emptypockets.spacemania.engine.systems.entitysystem.components;

import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class ComponentState<STATE_TYPE extends ComponentState> implements Poolable {
	public abstract void readComponentState(STATE_TYPE result);
	public abstract void writeComponentState(STATE_TYPE data);
	public abstract void reset();

	public boolean hasStateChanged(STATE_TYPE type) {
		return false;
	}
}
