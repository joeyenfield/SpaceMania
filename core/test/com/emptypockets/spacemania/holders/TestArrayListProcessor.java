package com.emptypockets.spacemania.holders;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class TestArrayListProcessor {

	static PrintWriter out;

	public static ArrayListProcessor<Data> createJobs(int dataCount, int jobSize) {
		ArrayListProcessor<Data> result = new ArrayListProcessor<Data>();
		for (int i = 0; i < dataCount; i++) {
			Data job = new Data();
			job.size = jobSize;
			job.id = i;
			result.add(job);
		}
		return result;
	}

	public static void logLine(int dataCount, int jobSize, float jobTime, float single, float[] thread) {
		StringBuilder result = new StringBuilder();
		result.append(dataCount);
		result.append(",");
		result.append(jobSize);
		result.append(",");
		result.append(jobTime);
		result.append(",");
		result.append(single);
		result.append(",");
		for (int i = 0; i < thread.length; i++) {
			result.append(thread[i]);
			result.append(",");
		}
		result.append("\n");
		System.out.print(result);
		out.append(result.toString());
	}

	
	public static void main(String input[]) throws FileNotFoundException{
//		int jobSize = 100;
//		int threads = 8;
//		
//		ArrayListProcessor<Data> data = createJobs(jobSize, 1);
//		long start = System.currentTimeMillis();
//		data.processParallel(new JobLoggingProcessor(), threads, 0);
//		System.out.println(System.currentTimeMillis()-start);
//		data.releaseThreads();
//	}
//	public static void performanceTesting(String[] args) throws FileNotFoundException {
		out = new PrintWriter(new BufferedOutputStream(new FileOutputStream("c:\\test\\stats.csv")));
		// int jobSize = 1000;
		// int dataCount = 1000;
		int avgCount = 1;
		int warmupCount = 1;
		int threadCount = 8;
		JobProcessor jobProcessor = new JobProcessor();
		JobTimeProcessCalculator jobTimeProcesser = new JobTimeProcessCalculator();

		long start = 0;
		long time = 0;

		int dataCountStart = 51200;
		int dataCountStep = 16;
		int dataCountEnd = 51200;
		
		int jobSizeStart = 50;
		int jobSizeStep = 5000;
		int jobSizeEnd = 50;
		
		for (int dataCount = dataCountStart; dataCount <= dataCountEnd; dataCount+=dataCountStep) {
			for (int jobSize = jobSizeStart; jobSize <= jobSizeEnd; jobSize+=jobSizeStep) {
				ArrayListProcessor<Data> data = createJobs(dataCount, jobSize);
				float singleTime = 0;
				float paralleTime[] = new float[threadCount];
				float jobTime = 0;

				for (int i = 0; i < avgCount + warmupCount; i++) {
					// Normal Single Processing
					start = System.currentTimeMillis();
					data.process(jobProcessor);
					time = System.currentTimeMillis() - start;
					if (i >= warmupCount) {
						singleTime += time / (float) avgCount * 1e-3;
					}

					// Multithreading
					for (int threads = 0; threads < threadCount; threads++) {
						start = System.currentTimeMillis();
						data.processParallel(jobProcessor, threads + 1);
						data.releaseThreads();
						time = System.currentTimeMillis() - start;
						if (i >= warmupCount) {
							paralleTime[threads] += time / (float) avgCount * 1e-3;
						}
					}
					jobTimeProcesser.totalTime = 0;
					data.process(jobTimeProcesser);
					jobTime += ((jobTimeProcesser.totalTime/dataCount) / (float) avgCount) * 1e-9;
				}
				logLine(dataCount, jobSize, jobTime, singleTime, paralleTime);
				out.flush();
				data.releaseThreads();
			}
		}
		out.close();
	}
}

class Data {
	int id = 0;
	int size = 1000;
	long processTime;
	float value = 0;
	boolean complete = false;
	
	@Override
	public String toString() {
		return Integer.toString(id)+" - "+processTime;
	}
}

class JobLoggingProcessor  implements SingleProcessor<Data> {
	@Override
	public void process(Data entity) {
		System.out.println("Starting Job : "+entity.id);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished Job : "+entity.id);
	}

}

class JobTimeProcessCalculator implements SingleProcessor<Data> {

	long totalTime = 0;

	@Override
	public void process(Data entity) {
		totalTime += entity.processTime;
	}

}

class JobProcessor implements SingleProcessor<Data> {

	@Override
	public void process(Data entity) {
		entity.processTime = System.currentTimeMillis();

		for (int i = 0; i < entity.size; i++) {
			entity.value += Math.sin(i);
		}

		entity.processTime = System.currentTimeMillis() - entity.processTime;
		entity.complete = true;
	}

}
