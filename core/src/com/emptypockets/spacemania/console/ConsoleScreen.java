package com.emptypockets.spacemania.console;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class ConsoleScreen extends Window implements ConsoleListener{
	Label label;
	ScrollPane scroll;
	StringBuffer console;
	int characterLimit = 1000000;
	TextButton maximise;
	
	public ConsoleScreen(String title, Skin skin) {
		super(title, skin);
	
		console= new StringBuffer("                                                                                                                                                                                                                ");
		createConsole(skin);
		Console.register(this);
	}
	
	public void createConsole(Skin skin){
		label= new Label("                                                                                                                                                                                                                "
				, skin);
		label.setAlignment(Align.top, Align.left);
		//lbl.setWrap(true);
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
	
	public ScrollPane getContent(){
		return scroll;
	}
	public void relayout(){
		clear();
		row();
		add(scroll).fill().expand();
		row();
		add(maximise).fillX();
		pack();
	}
	
	public void print(String message){
		console.insert(0, message);
		if(console.length() > characterLimit){
			console.setLength(characterLimit);
		}
		
		label.setText(console);
	}
	
	public void println(String message){
		print(message+"\n");
	}
	
	public void printf(String message, Object... values){
		print(String.format(message, values));
	}

}
