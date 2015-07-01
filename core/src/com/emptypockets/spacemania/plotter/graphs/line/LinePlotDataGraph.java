package com.emptypockets.spacemania.plotter.graphs.line;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.plotter.data.timeseries.TimeSeriesDataset;
import com.emptypockets.spacemania.plotter.data.timeseries.TimeSeriesPoint;

public class LinePlotDataGraph {
	String name;
	Rectangle screenBounds;

	HashMap<TimeSeriesDataset, LinePlotDescription> dataset = new HashMap<TimeSeriesDataset, LinePlotDescription>();
	float xMin;
	float yMin;
	float xMax;
	float yMax;
	boolean drawBounds = true;
	Color boundsColor = new Color(Color.WHITE);
	float boundsVerticalPadding = 3f;

	public LinePlotDataGraph(String name) {
		screenBounds = new Rectangle();
		this.name = name;
		boundsColor.a = 0.4f;
	}

	public void fitRange() {
		boolean first = true;
		for (TimeSeriesDataset data : dataset.keySet()) {
			if (first) {
				first = false;
				xMin = data.getMinTime();
				xMax = data.getMaxTime();
				yMin = data.getMinValue();
				yMax = data.getMaxValue();
			} else {
				xMin = Math.min(data.getMinTime(), xMin);
				xMax = Math.max(data.getMaxTime(), xMax);
				yMin = Math.min(data.getMinValue(), yMin);
				yMax = Math.max(data.getMaxValue(), yMax);
			}
		}
		if (xMin == xMax) {
			xMin -= 0.01 * xMax;
		}

		if (yMin == yMax) {
			yMin -= 0.01 * yMax;
		}
	}

	public void matchRange(LinePlotDataGraph graph) {
		this.xMin = graph.xMin;
		this.xMax = graph.xMax;
		this.yMin = graph.yMin;
		this.yMax = graph.yMax;
	}

	public void addLineDataset(TimeSeriesDataset data, LinePlotDescription plot) {
		data.sortData();
		this.dataset.put(data, plot);
	}

	public void updateLayout() {

		// System.out.println(name + "X - : " + xMin + " - " + xMax);
		// System.out.println(name + "Y - : " + yMin + " - " + yMax);
		for (TimeSeriesDataset data : dataset.keySet()) {
			layoutData(data, dataset.get(data).getPlotData());
		}
	}

	public void layoutData(TimeSeriesDataset dataset, ArrayList<Vector2> lineData) {
		// Fix Dataset
		if (lineData.size() != dataset.getPointCount()) {
			if (lineData.size() < dataset.getPointCount()) {
				// Need to add
				while (lineData.size() < dataset.getPointCount()) {
					lineData.add(new Vector2());
				}
			} else {
				// Need to remove
				while (lineData.size() > dataset.getPointCount()) {
					lineData.remove(lineData.size() - 1);
				}
			}
		}

		// Scale data
		for (int i = 0; i < dataset.getPointCount(); i++) {
			Vector2 point = lineData.get(i);

			// Scale dataset
			TimeSeriesPoint ts = dataset.getPoints().get(i);

			point.x = (ts.time - xMin) / (xMax - xMin);
			point.y = (ts.value - yMin) / (yMax - yMin);

			// Map to screen
			point.x = screenBounds.x + screenBounds.width * point.x;
			point.y = screenBounds.y + screenBounds.height * point.y;
		}
	}
	
	public float screenToGraphX(float screen){
		float pos = (screen-screenBounds.x)/(screenBounds.width);
		pos = xMin+(xMax-xMin)*pos;
		return pos;
	}
	
	public float screenToGraphY(float screen){
		float pos = (screen-screenBounds.y)/(screenBounds.height);
		pos = yMin+(yMax-yMin)*pos;
		return pos;
	}

	public void drawLine(ShapeRenderer shape) {
		shape.begin(ShapeType.Filled);
		shape.setColor(boundsColor);
		int src = GL20.GL_SRC_ALPHA;
		int dst = GL20.GL_ONE;
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(src, dst);

		if (drawBounds) {
			shape.rectLine(screenBounds.x, screenBounds.y - boundsVerticalPadding, screenBounds.x + screenBounds.width, screenBounds.y - boundsVerticalPadding, 1);
			shape.rectLine(screenBounds.x + screenBounds.width, screenBounds.y - boundsVerticalPadding, screenBounds.x + screenBounds.width, screenBounds.y + screenBounds.height + 2 * boundsVerticalPadding, 1);
			shape.rectLine(screenBounds.x + screenBounds.width, screenBounds.y + screenBounds.height + 2 * boundsVerticalPadding, screenBounds.x, screenBounds.y + screenBounds.height + 2 * boundsVerticalPadding, 1);
			shape.rectLine(screenBounds.x, screenBounds.y + screenBounds.height + 2 * boundsVerticalPadding, screenBounds.x, screenBounds.y - boundsVerticalPadding, 1);
		}
		shape.end();

		shape.begin(ShapeType.Filled);
		for (LinePlotDescription linePlot : dataset.values()) {
			if (linePlot.isDrawLine()) {
				shape.setColor(linePlot.getLineColor());
				for (int point = 0; point < linePlot.getPlotData().size() - 1; point++) {
					shape.rectLine(linePlot.getPlotData().get(point), linePlot.getPlotData().get(point + 1), linePlot.getLineSize());
				}
			}

			if (linePlot.isDrawPoint()) {
				shape.setColor(linePlot.getPointColor());
				for (int point = 0; point < linePlot.getPlotData().size(); point++) {
					Vector2 p = linePlot.getPlotData().get(point);
					shape.circle(p.x, p.y, linePlot.pointSize, 3);
				}
			}

			if (linePlot.isSpike()) {
				shape.setColor(linePlot.getPointColor());
				for (int point = 0; point < linePlot.getPlotData().size(); point++) {
					Vector2 p = linePlot.getPlotData().get(point);
					shape.rectLine(screenBounds.x + p.x, screenBounds.y, screenBounds.x + p.x, p.y, linePlot.getLineSize());
				}
			}
		}
		shape.end();
	}

	public Rectangle getScreenBounds() {
		return screenBounds;
	}

	public void setScreenBounds(Rectangle screenBounds) {
		this.screenBounds.set(screenBounds);
	}

	public void setxMin(float xMin) {
		this.xMin = xMin;
	}

	public void setyMin(float yMin) {
		this.yMin = yMin;
	}

	public void setxMax(float xMax) {
		this.xMax = xMax;
	}

	public void setyMax(float yMax) {
		this.yMax = yMax;
	}

	public String getName() {
		return name;
	}
}
