package com.emptypockets.spacemania.engine.spatialpartition;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.entitysystem.EntitySystemManager;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;

public abstract class GameEntitySpatialPartition extends EntitySystemManager implements SingleProcessor<GameEntity> {

	int partitionMask = ComponentType.PARTITION.getMask();

	public GameEntitySpatialPartition() {

	}

	public int getPartitionMask() {
		return partitionMask;
	}

	public void setPartitionMask(int partitionMask) {
		this.partitionMask = partitionMask;
	}

	public void manage(EntitySystem system, float deltaTime) {
		reset();
		system.process(this, partitionMask);
	}

	public abstract void reset();

	public abstract void search(Rectangle region, ArrayListProcessor<GameEntity> results);
}
