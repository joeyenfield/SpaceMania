package com.emptypockets.spacemania.engine.entitysystem.components.partition;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentData;
import com.emptypockets.spacemania.engine.spatialpartition.PartitionKey;

public class PartitionData extends ComponentData<PartitionData> {
	public float radius = 1;
	PartitionKey lastKey = new PartitionKey();
	PartitionKey key = new PartitionKey();

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

	@Override
	public void reset() {
		key.reset();
		lastKey.reset();
		radius = 1;
	}

}
