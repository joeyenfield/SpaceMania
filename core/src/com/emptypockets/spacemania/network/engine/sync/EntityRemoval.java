package com.emptypockets.spacemania.network.engine.sync;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class EntityRemoval implements Poolable {
	int id;
	Vector2 pos = new Vector2();
	boolean killed;
	
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
		this.pos.set(pos);
	}


	public boolean isKilled() {
		return killed;
	}


	public void setKilled(boolean killed) {
		this.killed = killed;
	}
}
