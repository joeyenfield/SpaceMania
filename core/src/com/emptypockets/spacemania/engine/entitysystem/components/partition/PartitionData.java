package com.emptypockets.spacemania.engine.entitysystem.components.partition;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;

public class PartitionData extends ComponentData<PartitionData> {
	public float radius = 1;

	@Override
	public void getComponentData(PartitionData result) {
		result.radius = radius;
	}

	@Override
	public void setComponentData(PartitionData data) {
		radius = data.radius;
	}

	@Override
	public boolean changed(PartitionData data) {
		return radius - data.radius > 0.1f;
	}

}
