package com.emptypockets.spacemania.utils;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.network.common.data.engine.entity.GameEntityRemoved;

public class PoolsManager {

	public synchronized static <T> T obtain(Class<T> classType) {
		return Pools.obtain(classType);
	}

	public synchronized static void free(Object o) {
		Pools.free(o);
	}

	public static void freeAll(ArrayList<?> data) {
		int size = data.size();
		for (int i = 0; i < size; i++) {
			free(data.get(i));
		}
		data.clear();
	}
}
