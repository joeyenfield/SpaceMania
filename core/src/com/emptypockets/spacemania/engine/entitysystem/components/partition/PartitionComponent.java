package com.emptypockets.spacemania.engine.entitysystem.components.partition;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

public class PartitionComponent extends EntityComponent<PartitionData> {

	public PartitionComponent(GameEntity entity) {
		super(entity, ComponentType.PARTITION, new PartitionData());
	}

	public void update(float deltaTime) {
	}

}
