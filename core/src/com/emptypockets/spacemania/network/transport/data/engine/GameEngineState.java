package com.emptypockets.spacemania.network.transport.data.engine;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.engines.GameEngine;
import com.emptypockets.spacemania.network.transport.data.engine.entity.EntitySystemState;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEngineState implements Poolable {
	public float serverTime;
	public EntitySystemState entitySystemState;
	
	@Override
	public void reset() {
		serverTime = 0;
		PoolsManager.free(entitySystemState);
		entitySystemState = null;
	}

	public void printIt(){
		System.out.println("\n##############################\nGameEngineState ["+serverTime+"]");
		entitySystemState.printIt();
	}
}
