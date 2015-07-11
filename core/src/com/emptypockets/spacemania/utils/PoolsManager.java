package com.emptypockets.spacemania.utils;

import com.badlogic.gdx.utils.Pools;

public class PoolsManager {

	public synchronized static <T>  T obtain(Class<T> classType){
		return Pools.obtain(classType);
	}
	
	public synchronized static void free(Object o){
		Pools.free(o);
	}
}

