package com.emptypockets.spacemania.plotter.data.timeseries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TimeSeriesDataset {

	String name;
	public float minValue;
	public float maxValue;

	public long maxTime;
	public long minTime;

	ArrayList<TimeSeriesPoint> datapoints = new ArrayList<TimeSeriesPoint>();

	public void add(Long time, float value) {
		TimeSeriesPoint point = new TimeSeriesPoint();
		point.time = time;
		point.value = value;
		add(point);
	}

	public ArrayList<TimeSeriesPoint> getPoints() {
		return datapoints;
	}

	public void sortData() {
		Collections.sort(datapoints, new Comparator<TimeSeriesPoint>() {

			@Override
			public int compare(TimeSeriesPoint o1, TimeSeriesPoint o2) {
				return (int) (o2.time - o1.time);
			}
		});
	}

	public void add(TimeSeriesPoint point) {
		if (datapoints.size() == 0) {
			minValue = point.value;
			maxValue = point.value;
			minTime = point.time;
			maxTime = point.time;
		} else {
			if (minValue > point.value) {
				minValue = point.value;
			}
			if (maxValue < point.value) {
				maxValue = point.value;
			}

			if (minTime > point.time) {
				minTime = point.time;
			}
			if (maxTime < point.time) {
				maxTime = point.time;
			}
		}

		datapoints.add(point);

	}

	public float getValueRange() {
		return maxValue - minValue;
	}

	public float getMinValue() {
		return minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public float getTimeRange() {
		return maxTime - minTime;
	}

	public Long getMinTime() {
		return minTime;
	}

	public Long getMaxTime() {
		return maxTime;
	}

	public void reset() {
		datapoints.clear();
	}

	public long getPointCount() {
		return datapoints.size();
	}

	public String printRange() {
		return "Value : [" + minValue + "," + maxValue + "] Range [" + minTime + "," + maxTime + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
