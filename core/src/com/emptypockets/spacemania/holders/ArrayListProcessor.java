package com.emptypockets.spacemania.holders;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.utils.Pool;
import com.emptypockets.spacemania.utils.PoolsManager;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ArrayListProcessor<ENT> extends ObjectProcessor<ENT> {
	ArrayList<ENT> holder;

	public ArrayListProcessor() {
		holder = new ArrayList<ENT>();
	}

	public synchronized int getSize() {
		return holder.size();
	}

	public synchronized void add(ENT entity) {
		holder.add(entity);
	}

	public synchronized void remove(ENT entity) {
		holder.remove(entity);
	}

	public synchronized void clear() {
		holder.clear();
	}

	@Override
	public Iterator<ENT> getIterator() {
		return holder.iterator();
	}

	@Override
	public synchronized void process(SingleProcessor<ENT> processor) {
		int size = holder.size();
		for (int i = 0; i < size; i++) {
			processor.process(holder.get(i));
		}
	}

	public synchronized void freeToPool() {
		int size = holder.size();
		for (int i = 0; i < size; i++) {
			PoolsManager.free(holder.get(i));
		}

		clear();
	}

}
