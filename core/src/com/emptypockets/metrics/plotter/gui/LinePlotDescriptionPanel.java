package com.emptypockets.metrics.plotter.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.emptypockets.metrics.plotter.graphs.line.LinePlotDescription;

public class LinePlotDescriptionPanel extends Tree {

	CheckBox enabled;
	String name;
	ColorNode pointColor;
	ColorNode lineColor;
	CheckBox lineEnabled;
	CheckBox pointEnabled;


	public LinePlotDescriptionPanel(Skin skin, String name) {
		super(skin);
		this.name= name;
		enabled = new CheckBox(name, skin);
		Node node = new MyNode(enabled);
		add(node);

		pointColor =new ColorNode("", skin);
		lineColor =new ColorNode("", skin);
		lineEnabled = new CheckBox("Line", skin);
		pointEnabled = new CheckBox("Point", skin);
		
		Node line = new MyNode(lineEnabled);
		line.add(lineColor);
		
		Node point = new MyNode(pointEnabled);
		point.add(pointColor);
		
		
		
		node.add(line);
		node.add(point);
		
	}
	
	public LinePlotDescription createDescription(){
		LinePlotDescription plotDescription = new LinePlotDescription();
		plotDescription.setDrawLine(lineEnabled.isChecked());
		plotDescription.setDrawPoint(pointEnabled.isChecked());
		plotDescription.setSpike(false);
		plotDescription.setLineColor(lineColor.color);
		plotDescription.setPointColor(pointColor.color);
		plotDescription.setName(name);
		return plotDescription;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled.isChecked();
	}

	public void resetValues() {
		pointEnabled.setChecked(true);
		lineEnabled.setChecked(true);
		pointColor.setColor(Color.WHITE);
		lineColor.setColor(Color.WHITE);
		enabled.setChecked(false);
	}

	public void readValues(LinePlotDescription desc) {
		pointEnabled.setChecked(desc.isDrawPoint());
		lineEnabled.setChecked(desc.isDrawLine());
		pointColor.setColor(desc.getPointColor());
		lineColor.setColor(desc.getLineColor());
		enabled.setChecked(true);
	}
}
