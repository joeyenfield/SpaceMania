package com.emptypockets.spacemania.network.common.data.engine;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.network.common.data.engine.entity.EntitySystemState;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEngineState implements Poolable, Comparable<GameEngineState> {
	public float serverTime;
	public int myPlayerId;
	public EntitySystemState entitySystemState;

	@Override
	public void reset() {
		serverTime = 0;
		PoolsManager.free(entitySystemState);
		entitySystemState = PoolsManager.obtain(EntitySystemState.class);
	}

	public void printIt() {
		System.out.println("\n##############################\nGameEngineState [" + serverTime + "]");
		entitySystemState.printIt();
	}

	@Override
	public int compareTo(GameEngineState o) {
		float delta = serverTime - o.serverTime;
		if (delta == 0) {
			return 0;
		} else if (delta > 0) {
			return -1;
		} else {
			return 1;
		}
	}
}
