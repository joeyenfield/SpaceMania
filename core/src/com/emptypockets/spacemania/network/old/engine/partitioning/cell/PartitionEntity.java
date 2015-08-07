package com.emptypockets.spacemania.network.old.engine.partitioning.cell;

import com.badlogic.gdx.math.Vector2;

public interface PartitionEntity {

	public Vector2 getPos();
	public float getRadius();
	
	public int getPartitionTag();
	public void setPartitionTag(int tag);
}
