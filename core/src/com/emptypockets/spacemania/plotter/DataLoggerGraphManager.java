package com.emptypockets.spacemania.plotter;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.emptypockets.spacemania.gui.tools.Scene2DToolkit;
import com.emptypockets.spacemania.plotter.graphs.line.LinePlotDataGraph;
import com.emptypockets.spacemania.plotter.graphs.line.LinePlotDescription;

public class DataLoggerGraphManager extends Window {
	LinePlotDataGraph graph;

	Button hideButton;

	Button applyButton;
	HashMap<String, CheckBox> buttons;
	HashMap<String, CheckBox> lineDraw;
	HashMap<String, CheckBox> shapeDraw;
	HashMap<String, CheckBox> spikeDraw;
	HashMap<String, TextField> colors;
	Stage stage;
	public DataLoggerGraphManager(Stage stage, String title, LinePlotDataGraph graph) {
		super(title, Scene2DToolkit.getToolkit().getSkin());
		buttons = new HashMap<String, CheckBox>();
		this.graph = graph;
		this.stage = stage;
		createPanel();
	}

	public void updateUI() {

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
				setVisible(false);
				stage.setScrollFocus(null);
			}
		});
		getTitleTable().add(hideButton).align(Align.right);

		Table table = new Table();
		for (final String name : DataLogger.getSortedData()) {
			final CheckBox checkbox = new CheckBox(name, skin);
			buttons.put(name, checkbox);
			
			
			table.row();
			table.add(checkbox).fillX();
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
		add(scroll).fill().expand();
	}

	public void apply() {
		float xMin = 0;
		float xMax = 0;
		xMin = graph.getxMin();
		xMax = graph.getxMax();
		graph.clearData();
		for (String name : buttons.keySet()) {
			Button button = buttons.get(name);
			if (button.isChecked()) {
				LinePlotDescription plotDescription = new LinePlotDescription();
				plotDescription.setDrawLine(true);
				plotDescription.setDrawPoint(true);
				plotDescription.setSpike(false);
				plotDescription.setLineColor(Color.RED);
				plotDescription.setPointColor(Color.RED);
				plotDescription.setName(name);

				graph.addLineDataset(DataLogger.read(name), plotDescription);
			}
		}
		graph.fitRange();
		graph.setxMin(xMin);
		graph.setxMax(xMax);
		
		graph.updateLayout();
	}
}
