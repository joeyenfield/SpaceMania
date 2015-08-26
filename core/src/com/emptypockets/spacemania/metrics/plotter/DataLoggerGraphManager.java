package com.emptypockets.spacemania.metrics.plotter;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.emptypockets.spacemania.gui.tools.Scene2DToolkit;
import com.emptypockets.spacemania.metrics.plotter.data.TimeSeriesDataset;
import com.emptypockets.spacemania.metrics.plotter.graphs.line.LinePlotDataGraph;
import com.emptypockets.spacemania.metrics.plotter.graphs.line.LinePlotDescription;
import com.emptypockets.spacemania.metrics.plotter.gui.LinePlotDescriptionPanel;

public class DataLoggerGraphManager extends Window {
	LinePlotDataGraph graph;

	Button hideButton;
	Button reloadButton;

	Button applyButton;
	HashMap<String, LinePlotDescriptionPanel> buttons;
	Table contentTable;

	Stage stage;

	public DataLoggerGraphManager(Stage stage, String title, LinePlotDataGraph graph) {
		super(title, Scene2DToolkit.getToolkit().getSkin());
		buttons = new HashMap<String, LinePlotDescriptionPanel>();
		this.graph = graph;
		this.stage = stage;
		createPanel();

	}

	public void updateUI() {
		relayout();
		for (String key : buttons.keySet()) {
			buttons.get(key).resetValues();
		}
		if (graph == null) {
			return;
		}
		for (TimeSeriesDataset data : graph.getDataset().keySet()) {
			LinePlotDescription desc = graph.getDataset().get(data);
			buttons.get(desc.getName()).readValues(desc);
		}
	}

	public void createPanel() {

		/**
		 * Setup close button
		 */
		TextButtonStyle style = new TextButtonStyle();
		style.font = new BitmapFont();
		style.fontColor = new Color(Color.WHITE);
		style.overFontColor = new Color(Color.DARK_GRAY);
		style.checkedFontColor = new Color(Color.GRAY);
		hideButton = new TextButton("X", style);
		hideButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (getHeight() == Gdx.graphics.getHeight()) {
					setHeight(getTitleTable().getHeight());
					setY(Gdx.graphics.getHeight() - getHeight());
				} else {
					setHeight(Gdx.graphics.getHeight());
				}
				stage.setScrollFocus(null);
			}
		});
		reloadButton = new TextButton("R", style);
		reloadButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				updateUI();
			}
		});
		getTitleTable().add(reloadButton).align(Align.left);
		getTitleTable().add(hideButton).align(Align.right);

		applyButton = new TextButton("Apply", Scene2DToolkit.skin());
		applyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				apply();
			}
		});
		
		contentTable = new Table();
		ScrollPane scrollControls = new ScrollPane(contentTable);
		row();
		add(applyButton).fillX();
		row();
		add(scrollControls).left().fill().expand();
		relayout();
	}

	public void relayout() {
		contentTable.clear();
		for (final String name : DataLogger.getSortedData()) {
			final LinePlotDescriptionPanel checkbox = new LinePlotDescriptionPanel(Scene2DToolkit.skin(), name);
			buttons.put(name, checkbox);
			contentTable.row();
			contentTable.add(checkbox).left().fillX().expandX();
		}
	}

	public void apply() {
		if(graph == null){
			return;
		}
		float xMin = 0;
		float xMax = 0;
		xMin = graph.getxMin();
		xMax = graph.getxMax();
		graph.clearData();
		for (String name : buttons.keySet()) {
			LinePlotDescriptionPanel button = buttons.get(name);
			if (button.isEnabled()) {
				graph.addLineDataset(DataLogger.read(name), button.createDescription());
			}
		}
		graph.fitRange();
		graph.setxMin(xMin);
		graph.setxMax(xMax);

		graph.updateLayout();
	}

	public void setGraph(LinePlotDataGraph graph) {
		this.graph = graph;
		updateUI();
	}
}
