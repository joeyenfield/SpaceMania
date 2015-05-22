package com.emptypockets.spacemania.network.engine.sync;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.network.engine.EntityState;
import com.emptypockets.spacemania.network.engine.EntityType;

public class EntityCreation implements Poolable {
	int id;
	EntityType type;
	EntityState entityState;
	
	
	public EntityState getEntityState() {
		return entityState;
	}

	public void setEntityState(EntityState entityState) {
		this.entityState = entityState;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	@Override
	public void reset() {
		id = 0;
		type = null;
	}

}
