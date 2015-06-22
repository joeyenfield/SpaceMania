package com.emptypockets.spacemania.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.console.ConsoleListener;

public class ConsoleScreen extends Window implements ConsoleListener {
	Label label;
	ScrollPane scroll;
	StringBuffer console;
	int characterLimit = 10000;
	TextButton maximise;

	public ConsoleScreen(Console con, String title, Skin skin) {
		super(title, skin);

		console = new StringBuffer("                                                                                                                                                                                                                ");
		createConsole(skin);
		con.register(this);
	}

	public void createConsole(Skin skin) {
		label = new Label("                                                                                                                                                                                                                ", skin);
		label.setAlignment(Align.top, Align.left);
		// lbl.setWrap(true);
		scroll = new ScrollPane(label, skin);
		maximise = new TextButton("Max", skin);

		relayout();

		maximise.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				relayout();
			}
		});
	}

	public ScrollPane getContent() {
		return scroll;
	}

	public void relayout() {
		clear();
		row();
		add(scroll).fill().expand();
		row();
		add(maximise).fillX();
		pack();
	}

	public void print(Console con, String message) {
		console.insert(0, message);
		if (console.length() > characterLimit) {
			console.setLength(characterLimit);
		}

		label.setText(console);
	}

	public void println(Console con, String message) {
		print(con, message + "\n");
	}

	public void printf(Console con, String message, Object... values) {
		print(con, String.format(message, values));
	}

}
