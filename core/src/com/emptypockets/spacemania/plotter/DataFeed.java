package com.emptypockets.spacemania.plotter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.emptypockets.spacemania.plotter.data.timeseries.TimeSeriesDataset;
import com.emptypockets.spacemania.plotter.data.timeseries.TimeSeriesPoint;

public class DataFeed {
	String name;

	TimeSeriesDataset dataset = new TimeSeriesDataset();
	File file;

	public DataFeed(File fileDAta) throws FileNotFoundException {
		this.file = fileDAta;
	}

	public TimeSeriesDataset read() throws IOException {
		dataset.reset();
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		dataset.setName(name);
		long count = in.readLong();

		for (int i = 0; i < count; i++) {
			TimeSeriesPoint point = new TimeSeriesPoint();
			point.time = in.readLong();
			point.value = in.readFloat();
			dataset.add(point);
		}
		in.close();
		return dataset;
	}

	public void add(long time, float value) {
		synchronized (dataset) {
			dataset.add(time, value);
		}
	}

	public void write() throws IOException {
		// Increment Length
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		// Add recored
		out.writeLong(dataset.getPointCount());
		synchronized (dataset) {
			for (TimeSeriesPoint point : dataset.getPoints()) {
				out.writeLong(point.time);
				out.writeFloat(point.value);
			}
		}
		out.flush();
		out.close();
		dataset.reset();
	}
}
