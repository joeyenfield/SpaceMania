package com.emptypockets.spacemania.command;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.console.ConsoleListener;
import com.emptypockets.spacemania.gui.Scene2DToolkit;
import com.emptypockets.spacemania.gui.ScreenSizeHelper;

public class CommandLinePanel extends Table implements ConsoleListener{
	static final float touchSize = ScreenSizeHelper.getcmtoPxlX(1);
	int characterLimit = 3000;
	Skin skin;
	TextField command;
	TextButton prevCommand;
	TextButton nextCommand;
	TextButton sendButton;
	Label consoleText;
	StringBuffer console;
	ScrollPane scroll;
	CommandLine commandLine;


	int currentCommand = 0;
	int commandHistory = 10;
	ArrayList<String> commands = new ArrayList<String>(commandHistory);
	
	
	public CommandLinePanel(CommandLine commandHub) {
		super(Scene2DToolkit.getToolkit().getSkin());
		this.commandLine = commandHub;
		createPanel();
		console = new StringBuffer();
		Console.register(this);
	}

	public void createPanel() {
		
		command = new TextField("", getSkin());
		sendButton = new TextButton("Send", getSkin());
		prevCommand = new TextButton("-", getSkin());
		nextCommand = new TextButton("+", getSkin());
		consoleText = new Label("", getSkin());
		scroll = new ScrollPane(consoleText, getSkin());
		
		row();
		add(prevCommand).height(touchSize).width(touchSize/2);
		add(nextCommand).height(touchSize).width(touchSize/2);
		add(command).expandX().fillX().height(touchSize);
		add(sendButton).height(touchSize).width(touchSize);
		row();
		add(scroll).colspan(4).expand().fill();
		prevCommand.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				previousCommand();
			}
		});
		nextCommand.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				nextCommand();
			}
		});
		command.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event instanceof InputEvent){
					InputEvent e = (InputEvent)event;
					if(e.getType() == Type.keyUp){
						int c= e.getKeyCode();
						if(c == 66){
							sendCommand();
						}
					}else if(e.getType() == Type.keyDown){
						int c= e.getKeyCode();
						if(c == 19){
							previousCommand();
						}else if(c == 20){
							nextCommand();
						}
					}
				}
				return false;
			}
		});
		sendButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sendCommand();
			}
		});

	}

	
	public void previousCommand(){
		currentCommand++;
		if(currentCommand > commands.size()-1){
			currentCommand = commands.size()-1;
		}
		command.setText(commands.get(currentCommand));
		command.setCursorPosition(command.getText().length());
	}

	public void nextCommand(){
		currentCommand--;
		if(currentCommand < 0){
			currentCommand = 0;
		}
		command.setText(commands.get(currentCommand));
		command.setCursorPosition(command.getText().length());
	}
	public void sendCommand(){
		String cmd = command.getText();
		sendCommand(cmd);
		command.setText("");
		command.setCursorPosition(0);
	}
	
	public void pushHistory(String cmd){
		currentCommand = -1;
		commands.add(0, cmd);
		if(commands.size() >= commandHistory){
			commands.remove(commands.size()-1);
		}	
	}
	
	public void sendCommand(String cmd){
		pushHistory(cmd);
		
		commandLine.processCommand(cmd);
	}

	public void print(String message){
		console.append(message);
		if(console.length() > characterLimit){
			int toRemove = console.length()-characterLimit;
			console.delete(0, toRemove);
			console.setLength(characterLimit);
		}
		consoleText.setText(console);
		scroll.setScrollbarsOnTop(true);
		scroll.validate();
		scroll.setScrollPercentY(100);
	}
	
	public void println(String message){
		print(message+"\n");
	}
	
	public void printf(String message, Object... values){
		print(String.format(message, values));
	}
}
