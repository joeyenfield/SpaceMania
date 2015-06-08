package com.emptypockets.spacemania.network.engine.sync;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class EntityRemoval implements Poolable {
	int id;
	Vector2 pos;
	
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


	public Vector2 getPos() {
		return pos;
	}


	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
}
