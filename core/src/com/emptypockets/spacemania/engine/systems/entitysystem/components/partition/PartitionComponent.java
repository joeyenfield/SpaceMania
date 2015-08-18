package com.emptypockets.spacemania.engine.systems.entitysystem.components.partition;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class PartitionComponent extends EntityComponent<PartitionData> {

	// Tag to prevent duplicate searchs
	public int currentSearchId;

	public PartitionComponent() {
		super(ComponentType.PARTITION);
	}

	public void update(float deltaTime) {
		if (data == null) {
			return;
		}
		entity.engine.spatialPartition.encodeRange(entity, data.key);
		// Region hasn't changed its ok
		if (data.key.equals(data.lastKey)) {
			return;
		}

		entity.engine.spatialPartition.moveEntity(entity, data.lastKey, data.key);
		data.lastKey.set(data.key);
	}

	@Override
	public Class<PartitionData> getDataClass() {
		return PartitionData.class;
	}

}
