package com.emptypockets.spacemania.engine.systems.entitysystem.components.partition;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;
import com.emptypockets.spacemania.engine.systems.spatialpartition.PartitionKey;

public class PartitionState extends ComponentState<PartitionState> {
	public float radius = 1;
	PartitionKey lastKey = new PartitionKey();
	PartitionKey key = new PartitionKey();

	@Override
	public void readComponentState(PartitionState result) {
		result.radius = radius;
	}

	@Override
	public void writeComponentState(PartitionState data) {
		radius = data.radius;
	}

	@Override
	public boolean hasStateChanged(PartitionState data) {
		return radius - data.radius > 0.1f;
	}

	@Override
	public void reset() {
		key.reset();
		lastKey.reset();
		radius = 1;
	}

}
