package com.emptypockets.metrics.plotter;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.emptypockets.metrics.plotter.data.TimeSeriesDataset;

public class DataLogger {
	String dataDirectory = "c:\\test\\data";
	String ext = "dat";

	static boolean enabled = false;
	static boolean nanoTime = false;

	static DataLogger logger;
	static long startTime = 0;
	HashMap<String, DataFeed> outputs;

	private DataLogger() throws FileNotFoundException {
		outputs = new HashMap<String, DataFeed>();
	}

	public void reset() throws IOException {
		Path directory = Paths.get(dataDirectory);
		Files.createDirectories(directory);
		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

		});
		Files.createDirectories(directory);
	}

	public String getDataFileName(String name) {
		return dataDirectory + "\\" + name + "." + ext;
	}

	public File getDataFile(String name) {
		return new File(getDataFileName(name));
	}

	public DataFeed createStream(String name) throws IOException {
		DataFeed out = new DataFeed(getDataFile(name));
		return out;
	}

	public DataFeed getDataFeed(String name) throws IOException {
		if (!outputs.containsKey(name)) {
			outputs.put(name, createStream(name));
		}
		return outputs.get(name);
	}

	public static DataLogger getDataLogger() throws FileNotFoundException {
		if (logger == null) {
			synchronized (DataLogger.class) {
				if (logger == null) {
					logger = new DataLogger();
					startTime = logger.getTime();
				}
			}
		}
		return logger;
	}

	public long getTime() {
		long current = 0;
		if (nanoTime) {
			current = System.nanoTime();
		} else {
			current = System.currentTimeMillis();
		}
		long time = current - startTime;
		return time;
	}

	public void readTimeSeries(String name) throws FileNotFoundException {
		DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(getDataFile(name))));
	}

	public static TimeSeriesDataset read(String name) {
		try {
			return getDataLogger().getDataFeed(name).read();
		} catch (IOException e) {
			e.printStackTrace();
			return new TimeSeriesDataset();
		}
	}

	private void writeAll() throws IOException {
		for (String key : outputs.keySet()) {
			DataFeed out = outputs.get(key);
			out.write();
		}
		outputs.clear();
	}

	public static void log(String name, float value) {
		if (!enabled) {
			return;
		}
		try {
			long time = getDataLogger().getTime();
			getDataLogger().getDataFeed(name).add(time, value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write() throws FileNotFoundException, IOException {
		if (!enabled) {
			return;
		}
		getDataLogger().writeAll();
	}

	public static void clean() {
		if (!enabled) {
			return;
		}
		try {
			getDataLogger().reset();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public Set<String> getDataLists() {
		return getFiles(dataDirectory, ext);
	}

	public Set<String> getFiles(String directory, String ext) {
		Set<String> textFiles = new HashSet<String>();
		File dir = new File(directory);
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith((ext))) {

				textFiles.add(stripExtension(file.getName()));
			}
		}
		return textFiles;
	}

	static String stripExtension(String str) {
		// Handle null case specially.

		if (str == null)
			return null;

		// Get position of last '.'.

		int pos = str.lastIndexOf(".");

		// If there wasn't any '.' just return the string as is.

		if (pos == -1)
			return str;

		// Otherwise return the string, up to the dot.

		return str.substring(0, pos);
	}

	public static List<String> getSortedData() {
		ArrayList<String> result = new ArrayList<String>(getData());
		Collections.sort(result);
		return result;
	}

	public static Set<String> getData() {
		try {
			return getDataLogger().getDataLists();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new HashSet();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		DataLogger.clean();
		for (int i = 0; i < 100; i++) {
			DataLogger.log("test", i);
			Thread.sleep(10);
		}
		DataLogger.write();

		TimeSeriesDataset data = DataLogger.read("test");
		data.printRange();
	}

	public synchronized static void startup() {
		enabled = true;
		clean();
	}

	public synchronized static void shutdown() {
		try {
			write();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		enabled = false;
	}

	public synchronized static boolean isEnabled() {
		return enabled;
	}

}
