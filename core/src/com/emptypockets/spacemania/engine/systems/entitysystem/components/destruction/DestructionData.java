package com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentData;

public class DestructionData extends ComponentData<DestructionData> {
	public boolean remove = false;
	public float destroyTime = 0;

	@Override
	public void getComponentData(DestructionData result) {
		result.remove = remove;
		result.destroyTime = destroyTime;
	}

	@Override
	public void setComponentData(DestructionData data) {
		remove = data.remove;
		destroyTime = data.destroyTime;
	}

	@Override
	public boolean changed(DestructionData data) {
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
