package com.emptypockets.spacemania.plotter.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ColorNode extends Node {

	Color color = new Color(Color.WHITE);

	Slider redSlider;
	Slider greenSlider;
	Slider blueSlider;
	Slider alphaSlider;

	public Color getSelectedColor() {
		return color;
	}

	public ColorNode(String name, Skin skin) {
		super(new Actor() {

			Texture texture;

			public void draw(Batch batch, float parentAlpha) {
				if (texture == null) {
					Pixmap map = new Pixmap(1, 1, Format.RGBA8888);
					map.drawPixel(0, 0, Color.WHITE.toIntBits());
					texture = new Texture(map);
				}
				batch.setColor(getColor());
				batch.draw(texture, getX(), getY(), getWidth(), getHeight());
			};
		});
		redSlider = new Slider(0, 1, 0.01f, false, skin);
		greenSlider = new Slider(0, 1, 0.01f, false, skin);
		blueSlider = new Slider(0, 1, 0.01f, false, skin);
		alphaSlider = new Slider(0, 1, 0.1f, false, skin);
		getActor().setSize(20, 20);
		alphaSlider.setValue(1f);
		add(new Node(redSlider));
		add(new Node(greenSlider));
		add(new Node(blueSlider));
		add(new Node(alphaSlider));

		InputListener stopTouchDown = new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		};
		redSlider.addListener(stopTouchDown);
		greenSlider.addListener(stopTouchDown);
		blueSlider.addListener(stopTouchDown);
		alphaSlider.addListener(stopTouchDown);

		ChangeListener change = new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				updateColor();
			};
		};
		redSlider.addListener(change);
		greenSlider.addListener(change);
		blueSlider.addListener(change);
		alphaSlider.addListener(change);
	}

	public void updateColor() {
		color.r = redSlider.getValue();
		color.g = greenSlider.getValue();
		color.b = blueSlider.getValue();
		color.a = alphaSlider.getValue();
		getActor().setColor(color);
	}

	public void setColor(Color col) {
		redSlider.setValue(col.r);
		greenSlider.setValue(col.g);
		blueSlider.setValue(col.b);
		alphaSlider.setValue(col.a);
	}
}
