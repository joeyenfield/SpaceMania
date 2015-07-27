package com.emptypockets.spacemania.holders;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.utils.Pool;
import com.emptypockets.spacemania.utils.PoolsManager;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ArrayListProcessor<ENT> extends ObjectProcessor<ENT> {
	class ThreadRunner extends Thread {
		public int offset;
		public int processDelta = 1;
		SingleProcessor<ENT> processor = null;

		public void process(SingleProcessor<ENT> processor, int offset, int processDelta) {
			this.processor = processor;
			this.offset = offset;
			this.processDelta = processDelta;
			synchronized (this) {
				notify();
			}
		}

		@Override
		public void run() {
			while (true) {
				if (processor != null && offset < holder.size()) {
					processor.process(holder.get(offset));
					offset += processDelta;
				} else {
					processor = null;
					synchronized (this) {
						synchronized (updateLock) {
							activeThreadCount--;
							if (activeThreadCount <= 0) {
								updateLock.notify();
							}
						}
						runnerPool.free(this);
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}
	}

	Object updateLock = new Object();
	int activeThreadCount = 0;

	Pool<ThreadRunner> runnerPool = new Pool<ArrayListProcessor<ENT>.ThreadRunner>() {
		@Override
		protected ArrayListProcessor<ENT>.ThreadRunner newObject() {
			ThreadRunner runner = new ThreadRunner();
			runner.start();
			return runner;
		}

	};
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

	@Override
	public synchronized void processParallel(SingleProcessor<ENT> processor, int maxCores, int timeoutSeconds) throws InterruptedException {
		for (int i = 0; i < maxCores; i++) {
			ThreadRunner runner = runnerPool.obtain();
			synchronized (updateLock) {
				activeThreadCount++;
			}
			runner.process(processor, i, maxCores);
		}
		synchronized (updateLock) {
			while (activeThreadCount > 0) {
				updateLock.wait();
			}
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
