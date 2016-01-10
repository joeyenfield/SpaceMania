package com.emptypockets.spacemania.network.engine.entity;

import com.badlogic.gdx.utils.Pool.Poolable;

public class GameEntityRemoved implements Poolable{
	public int id;

	@Override
	public void reset() {
		id = 0;
	}
}
