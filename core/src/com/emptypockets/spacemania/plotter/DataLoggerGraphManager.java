package com.emptypockets.spacemania.plotter;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.emptypockets.spacemania.gui.tools.Scene2DToolkit;
import com.emptypockets.spacemania.plotter.data.timeseries.TimeSeriesDataset;
import com.emptypockets.spacemania.plotter.graphs.line.LinePlotDataGraph;
import com.emptypockets.spacemania.plotter.graphs.line.LinePlotDescription;
import com.emptypockets.spacemania.plotter.gui.LinePlotDescriptionPanel;

public class DataLoggerGraphManager extends Window {
	LinePlotDataGraph graph;

	Button hideButton;

	Button applyButton;
	HashMap<String, LinePlotDescriptionPanel> buttons;

	Stage stage;

	public DataLoggerGraphManager(Stage stage, String title, LinePlotDataGraph graph) {
		super(title, Scene2DToolkit.getToolkit().getSkin());
		buttons = new HashMap<String, LinePlotDescriptionPanel>();
		this.graph = graph;
		this.stage = stage;
		createPanel();
		
	}

	public void updateUI() {
		for(String key : buttons.keySet()){
			buttons.get(key).resetValues();
		}
		for(TimeSeriesDataset data : graph.getDataset().keySet()){
			LinePlotDescription desc = graph.getDataset().get(data);
			buttons.get(desc.getName()).readValues(desc);
		}
	}

	public void createPanel() {
		Skin skin = Scene2DToolkit.skin();

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
					setY(Gdx.graphics.getHeight()-getHeight());
				} else {
					setHeight(Gdx.graphics.getHeight());
				}
				stage.setScrollFocus(null);
			}
		});
		getTitleTable().add(hideButton).align(Align.right);

		Table table = new Table();
		for (final String name : DataLogger.getSortedData()) {
			final LinePlotDescriptionPanel checkbox = new LinePlotDescriptionPanel(skin, name);
			buttons.put(name, checkbox);
			table.row();
			table.add(checkbox).left().fillX().expandX();
		}
		ScrollPane scroll = new ScrollPane(table);
		
		applyButton = new TextButton("Apply", skin);
		applyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				apply();
			}
		});
		row();
		add(applyButton).fillX();
		row();
		add(scroll).left().fill().expand();
	}

	public void apply() {
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