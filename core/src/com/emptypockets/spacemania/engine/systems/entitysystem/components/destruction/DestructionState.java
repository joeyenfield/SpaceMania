package com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;

public class DestructionState extends ComponentState<DestructionState> {
	public boolean remove = false;
	public float destroyTime = 0;

	@Override
	public void readComponentState(DestructionState result) {
		result.remove = remove;
		result.destroyTime = destroyTime;
	}

	@Override
	public void writeComponentState(DestructionState data) {
		remove = data.remove;
		destroyTime = data.destroyTime;
	}

	@Override
	public boolean hasStateChanged(DestructionState data) {
		if (remove != data.remove) {
			return true;
		}

		if (destroyTime != data.destroyTime) {
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		remove = false;
		destroyTime = 0;
	}

}
