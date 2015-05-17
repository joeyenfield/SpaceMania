package com.emptypockets.spacemania.commandLine;


import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.console.ConsoleListener;
import com.emptypockets.spacemania.gui.tools.Scene2DToolkit;

public class CommandLinePanel extends Table implements ConsoleListener, Disposable {
    float touchSize = 50;
    int characterLimit = 300000;
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

    public CommandLinePanel(CommandLine commandHub, int minTouchSize) {
        super(Scene2DToolkit.getToolkit().getSkin());
        this.commandLine = commandHub;
        touchSize = minTouchSize;
        createPanel();
        console = new StringBuffer();
        commandHub.getConsole().register(this);
    }

    public void createPanel() {

        command = new TextField("", getSkin());
        sendButton = new TextButton("Send", getSkin());
        prevCommand = new TextButton("-", getSkin());
        nextCommand = new TextButton("+", getSkin());
        consoleText = new Label("", getSkin());
        scroll = new ScrollPane(consoleText, getSkin());
        row();
        add(prevCommand).height(touchSize).width(touchSize);
        add(nextCommand).height(touchSize).width(touchSize);
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
                if (event instanceof InputEvent) {
                    InputEvent e = (InputEvent) event;
                    if (e.getType() == Type.keyUp) {
                        int c = e.getKeyCode();
                        if (c == 66) {
                            sendCommand();
                        }
                    } else if (e.getType() == Type.keyDown) {
                        int c = e.getKeyCode();
                        if (c == 19) {
                            previousCommand();
                        } else if (c == 20) {
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


    public void previousCommand() {
        currentCommand++;
        if (currentCommand > commandLine.getHistoryCount() - 1) {
            currentCommand = commandLine.getHistoryCount() - 1;
        }
        command.setText(commandLine.getHistory(currentCommand));
        command.setCursorPosition(command.getText().length());
    }

    public void nextCommand() {
        currentCommand--;
        if (currentCommand < 0) {
            currentCommand = 0;
        }
        command.setText(commandLine.getHistory(currentCommand));
        command.setCursorPosition(command.getText().length());
    }

    public void sendCommand() {
        String cmd = command.getText();
        sendCommand(cmd);
        command.setText("");
        command.setCursorPosition(0);
    }

    public void sendCommand(String cmd) {
        currentCommand = -1;
        commandLine.processCommand(cmd);
    }

    public void print(Console con, final String message) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                console.append(message);
                if (console.length() > characterLimit) {
                    int toRemove = console.length() - characterLimit;
                    console.delete(0, toRemove);
                    console.setLength(characterLimit);
                }
                consoleText.setText(console.toString());
                scroll.setScrollbarsOnTop(true);
                scroll.setScrollPercentY(1);

            }
        });
    }

    public void println(Console con, String message) {
        print(con,message + "\n");
    }

    public void printf(Console con, String message, Object... values) {
        print(con,String.format(message, values));
    }

    @Override
    public void dispose() {
    	 this.commandLine.getConsole().unregister(this);
    }
}
