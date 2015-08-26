package com.emptypockets.spacemania.metrics.plotter.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.emptypockets.spacemania.utils.PoolsManager;

public class TimeSeriesDataset {

	int maxDataCount = 0;

	String name;
	public float minValue;
	public float maxValue;

	public long maxTime;
	public long minTime;

	ArrayList<TimeSeriesPoint> datapoints = new ArrayList<TimeSeriesPoint>();

	public void add(Long time, float value) {
		TimeSeriesPoint point = PoolsManager.obtain(TimeSeriesPoint.class);
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

	public void updateRange() {
		minValue = 0;
		maxValue = 1;
		minTime = 0;
		maxTime = 1;

		int size = datapoints.size();
		for (int i = 0; i < datapoints.size(); i++) {
			TimeSeriesPoint point = datapoints.get(i);
			if (minValue > point.value || i == 0) {
				minValue = point.value;
			}
			if (maxValue < point.value || i == 0) {
				maxValue = point.value;
			}

			if (minTime > point.time || i == 0) {
				minTime = point.time;
			}
			if (maxTime < point.time || i == 0) {
				maxTime = point.time;
			}
		}
	}

	public void add(TimeSeriesPoint point) {
		datapoints.add(point);
		if (maxDataCount != 0 && datapoints.size() > maxDataCount) {
			PoolsManager.free(datapoints.remove(0));
		}
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
	
	public void printRange(){
		System.out.println(getPointCount()+" : "+getRange());
	}

	public String getRange() {
		return "Value : [" + minValue + "," + maxValue + "] Range [" + minTime + "," + maxTime + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
