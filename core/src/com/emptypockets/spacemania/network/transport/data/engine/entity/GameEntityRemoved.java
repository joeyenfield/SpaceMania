package com.emptypockets.spacemania.network.transport.data.engine.entity;

import com.badlogic.gdx.utils.Pool.Poolable;

public class GameEntityRemoved implements Poolable{
	public long id;

	@Override
	public void reset() {
		id = 0;
	}
}
