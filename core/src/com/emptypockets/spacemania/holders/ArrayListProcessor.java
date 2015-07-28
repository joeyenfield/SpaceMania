package com.emptypockets.spacemania.holders;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.utils.Pool;
import com.emptypockets.spacemania.utils.PoolsManager;

/**
 * Created by jenfield on 15/05/2015.
 */
public class ArrayListProcessor<ENT> extends ObjectProcessor<ENT> {
	static int count = 0;

	class ThreadRunner extends Thread {
		int num = count++;
		public int offset;
		public int processDelta = 1;
		SingleProcessor<ENT> processor = null;
		boolean alive = true;

		public synchronized void process(SingleProcessor<ENT> processor, int offset, int processDelta) {
			System.out.println("PROCESS[" + num + "] ++" + activeThreadCount);
			this.processor = processor;
			this.offset = offset;
			this.processDelta = processDelta;
			synchronized (activeThreadCountLock) {
				activeThreadCount++;
				System.out.println("PROCESS[" + num + "] ++" + activeThreadCount);
			}
			notify();
		}

		@Override
		public void run() {
			System.out.println("Processing [" + num + "] : START");
			synchronized (this) {
				while (alive) {
					try {
						System.out.println("Processing [" + num + "] : FREE");
						runnerPool.free(this);
						System.out.println("Processing [" + num + "] : WAIT");
						wait();
						if (processor != null && offset < holder.size()) {
							System.out.println("Processing [" + num + "]");
							for (; offset < holder.size(); offset += processDelta) {
								processor.process(holder.get(offset));
							}
							System.out.println("Processing [" + num + "] Finished");
							processor = null;
							synchronized (activeThreadCountLock) {
								activeThreadCount--;
								if (activeThreadCount <= 0) {
									activeThreadCountLock.notify();
								}
								System.out.println("Processing [" + num + "]--" + activeThreadCount);
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	Object activeThreadCountLock = new Object();
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

	private void setupWorkerThreads(int count) {
		for (int i = 0; i < count; i++) {
			System.out.println("SETUP" + i);
			ThreadRunner runner = new ThreadRunner();
			runner.start();
			runnerPool.free(runner);
		}
	}

	@Override
	public synchronized void processParallel(SingleProcessor<ENT> processor, int threads) {
		System.out.println("Processing Theads [" + threads + "]");
		if (runnerPool.getFree() < threads) {
			setupWorkerThreads(threads - runnerPool.getFree());
		}
		for (int i = 0; i < threads; i++) {
			System.out.println("Start Process [" + i + "]");
			runnerPool.obtain().process(processor, i, threads);
		}
		synchronized (activeThreadCountLock) {
			while (activeThreadCount > 0) {
				try {
					activeThreadCountLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

	public synchronized void releaseThreads() {
		while (runnerPool.getFree() > 0) {
			ThreadRunner runner = runnerPool.obtain();
			runner.alive = false;
			runner.process(null, 0, 0);
		}

	}
}
