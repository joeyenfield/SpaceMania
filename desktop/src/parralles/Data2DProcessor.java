package parralles;

import java.util.ArrayList;

public class Data2DProcessor {
	ArrayList<Runner> waitingRunners = new ArrayList<Data2DProcessor.Runner>();

	class Runner extends Thread {
		int posX;
		int posY;

		int startX;
		int startY;

		int endX;
		int endY;

		int stepX;
		int stepY;

		Data2DOperation operation = null;
		boolean alive = true;

		private void process() {
			for (posX = startX; posX < endX; posX += stepX) {
				for (posY = startY; posY < endY; posY += stepY) {
					operation.process(posX, posY);
				}
			}
		}

		@Override
		public void run() {
			while (alive) {
				if (operation != null) {
					process();
				}
				operation = null;
				synchronized (this) {
					try {
						synchronized (waitingRunners) {
							waitingRunners.add(this);
						}
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void process(Data2DOperation operation, int threads) {
		for (int thread = 0; thread < threads; thread++) {
			Runner runner = null;
			synchronized (waitingRunners) {
				if (waitingRunners.size() == 0) {
					runner = new Runner();
					runner.operation = operation;
					runner.start();
				} else {
					runner = waitingRunners.remove(waitingRunners.size() - 1);
					runner.operation = operation;
				}
				
				
			}
		}
	}

}
