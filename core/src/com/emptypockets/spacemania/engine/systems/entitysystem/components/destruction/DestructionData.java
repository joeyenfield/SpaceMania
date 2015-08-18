package com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentData;

public class DestructionData extends ComponentData<DestructionData> {
	public boolean remove = false;
	public long destroyTime = 0;
	@Override
	public void getComponentData(DestructionData result) {
	}

	@Override
	public void setComponentData(DestructionData data) {
	}

	@Override
	public boolean changed(DestructionData data) {
		return false;
	}

	@Override
	public void reset() {
		remove = false;
		destroyTime = 0;
	}

}
