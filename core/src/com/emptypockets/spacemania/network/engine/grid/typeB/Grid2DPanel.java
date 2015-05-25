package com.emptypockets.spacemania.network.engine.grid.typeB;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class Grid2DPanel extends Window {
	TextButton applyButton;
	TextButton resetButton;
	TextField sizeX;
	TextField sizeY;
	TextField numX;
	TextField numY;
//	TextField mass;
//	TextField damping;
//	TextField stiffness;
	TextField subSampleCount;
	CheckBox subSample;
	GridData2D background;
	
	public Grid2DPanel(GridData2D background, Skin skin) {
		super("Grid Settings", skin);
		this.background = background;
		createTable(skin);
		setKeepWithinStage(false);
	}

	public void createTable(Skin skin) {
		applyButton = new TextButton("Apply", skin);
		resetButton = new TextButton("Reset", skin);
//		mass = new TextField("1", skin);
		numX = new TextField("50", skin);
		numY = new TextField("50", skin);
		sizeX = new TextField("2000", skin);
		sizeY = new TextField("2000", skin);
//		damping = new TextField("0.9", skin);
//		stiffness = new TextField("15", skin);
		subSample = new CheckBox("Sub Sample", skin);
		subSampleCount = new TextField("1", skin);
		row();
		add("Size X ");
		add(sizeX).expandX().fillX();
		add("Size Y ");
		add(sizeY).expandX().fillX();

		row();
		add("Num X ");
		add(numX).expandX().fillX();

		add("Num Y ");
		add(numY).expandX().fillX();

////		row();
//		add("Damp");
//		add(damping).expandX().fillX();
//
//		add("Stiff");
//		add(stiffness).expandX().fillX();

		row();
//		add("Mass");
//		add(mass).expandX().fillX();
		add(subSample);
		add(subSampleCount).expandX().fillX();
		row();
		add(applyButton).colspan(4).fill().expand();

		row();
		add(resetButton).colspan(4).fill().expand();

		resetButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				background.resetNodes();
			}
		});
		applyButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				applyToGrid();
			}
		});
	}

	public void setGridSize(float width, float height){
		sizeX.setText(width+"");
		sizeY.setText(height+"");
	}
	public void applyToGrid() {
		Grid2DSettings settings = background.getSettings();
		settings.bounds.width = Float.parseFloat(sizeX.getText());
		settings.bounds.height = Float.parseFloat(sizeY.getText());
		settings.bounds.x = -settings.bounds.width / 2;
		settings.bounds.y = -settings.bounds.height / 2;
		settings.numX = Integer.parseInt(numX.getText());
		settings.numY = Integer.parseInt(numY.getText());
		
//		settings.inverseMass = 1 / Float.parseFloat(mass.getText());
//		settings.edge.stiffness = Float.parseFloat(stiffness.getText());
//		settings.edge.damping = Float.parseFloat(damping.getText());
//		settings.links.stiffness = Float.parseFloat(stiffness.getText());
//		settings.links.damping = Float.parseFloat(damping.getText());
//		
		background.createGrid(settings);
		background.subSample = subSample.isChecked();
		background.sampleCount = Integer.parseInt(subSampleCount.getText());
		
		sizeX.getStage().setKeyboardFocus(null);
	}
}