package com.emptypockets.spacemania.network.common.data.engine.entity;

import com.badlogic.gdx.utils.Pool.Poolable;

public class GameEntityRemoved implements Poolable{
	public int id;

	@Override
	public void reset() {
		id = 0;
	}
}
