package com.emptypockets.spacemania.plotter.graphs.line;

import com.badlogic.gdx.graphics.Color;
import com.emptypockets.spacemania.plotter.graphs.PlotData;

public class LinePlotDescription extends PlotData {
	String name = "";
	Color lineColor = new Color(Color.WHITE);
	float lineSize = 1;
	Color pointColor = new Color(Color.WHITE);
	float pointSize = 3;
	boolean drawLine = true;
	boolean drawPoint = true;
	boolean spike = false;

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor.set(lineColor);
	}

	public float getLineSize() {
		return lineSize;
	}

	public void setLineSize(float lineSizFe) {
		this.lineSize = lineSize;
	}

	public Color getPointColor() {
		return pointColor;
	}

	public void setPointColor(Color pointColor) {
		this.pointColor.set(pointColor);
	}

	public float getPointSize() {
		return pointSize;
	}

	public void setPointSize(float pointSize) {
		this.pointSize = pointSize;
	}

	public boolean isDrawLine() {
		return drawLine;
	}

	public void setDrawLine(boolean drawLine) {
		this.drawLine = drawLine;
	}

	public boolean isDrawPoint() {
		return drawPoint;
	}

	public void setDrawPoint(boolean drawPoint) {
		this.drawPoint = drawPoint;
	}

	public boolean isSpike() {
		return spike;
	}

	public void setSpike(boolean spike) {
		this.spike = spike;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
