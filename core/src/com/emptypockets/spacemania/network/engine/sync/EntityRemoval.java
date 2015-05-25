package com.emptypockets.spacemania.network.engine.sync;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.network.engine.entities.EntityType;

public class EntityRemoval implements Poolable {
	int id;
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void reset() {
		id = 0;
	}
}
