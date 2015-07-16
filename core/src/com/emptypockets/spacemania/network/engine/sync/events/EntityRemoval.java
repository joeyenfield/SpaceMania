package com.emptypockets.spacemania.network.engine.sync.events;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class EntityRemoval implements Poolable {
	int id;
	public Vector2 pos = new Vector2();
	public boolean killed;
	public boolean explodes;
	
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


	public boolean isExplodes() {
		return explodes;
	}


	public void setExplodes(boolean explodes) {
		this.explodes = explodes;
	}
}
