package com.emptypockets.spacemania.engine.entitysystem.components.partition;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

public class PartitionComponent extends EntityComponent<PartitionData> {

	// Tag to prevent duplicate searchs
	public int currentSearchId;

	public PartitionComponent() {
		super(ComponentType.PARTITION, new PartitionData());
	}

	public void reset() {
	}

	public void update(float deltaTime) {
		entity.engine.spatialPartition.encodeRange(entity, data.key);
		// Region hasn't changed its ok
		if (data.key.equals(data.lastKey)) {
			return;
		}

		entity.engine.spatialPartition.moveEntity(entity, data.lastKey, data.key);
		data.lastKey.set(data.key);
	}

}
