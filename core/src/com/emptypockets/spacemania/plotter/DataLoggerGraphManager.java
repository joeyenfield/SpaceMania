package com.emptypockets.spacemania.plotter;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.emptypockets.spacemania.gui.tools.Scene2DToolkit;
import com.emptypockets.spacemania.plotter.graphs.line.LinePlotDataGraph;

public class DataLoggerGraphManager extends Window {
	LinePlotDataGraph graph;

	public DataLoggerGraphManager(String title, LinePlotDataGraph graph) {
		super(title, Scene2DToolkit.getToolkit().getSkin());
		this.graph = graph;
		createPanel();
	}

	public void createPanel() {
		TextButton button = new TextButton("Apply", Scene2DToolkit.skin());

		row();
		add(button).fillX();

		
//		ScrollPane scroll = new ScrollPane();
		
		row().fill().expand();
	}

}
