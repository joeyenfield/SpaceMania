package com.emptypockets.spacemania.network.common.data.engine.entity;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.utils.PoolsManager;

public class EntitySystemState implements Poolable{

	public ArrayList<GameEntityRemoved> removedEntities = new ArrayList<GameEntityRemoved>();
	public ArrayList<GameEntityAdded> addedEntities = new ArrayList<GameEntityAdded>();
	public ArrayList<GameEntityNetworkSync> entityStates = new ArrayList<GameEntityNetworkSync>();
	
	@Override
	public void reset() {
		PoolsManager.freeAll(removedEntities);
		PoolsManager.freeAll(addedEntities);
		PoolsManager.freeAll(entityStates);
	}

	public void printIt() {
		System.out.println("To Remove : ");
		for(GameEntityRemoved removed : removedEntities){
			System.out.println(" "+removed.id);
		}
		
		System.out.println("Added : ");
		for(GameEntityAdded added : addedEntities){
			System.out.println(" "+added.id+" - "+added.type);
		}
		
		System.out.println("GameEntityNetworkSync : ");
		for(GameEntityNetworkSync state : entityStates){
			state.printId();
		}
	}
}
