package com.emptypockets.spacemania.commandLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import com.emptypockets.spacemania.console.Console;

public class CommandLine {
    int commandHistoryCount = 10;
    HashMap<String, Command> commandData;
    ArrayList<String> commandHistory = new ArrayList<String>(commandHistoryCount);

    boolean echoCommand = false;
    boolean echoSplitCommand = true;
    Console console;
    
    public CommandLine(Console console) {
    	this.console = console;
        commandData = new HashMap<String, Command>();
        registerCommand(new com.emptypockets.spacemania.commandLine.commands.CommandLineHelpCommand(this));
    }

    public Console getConsole(){
    	return console;
    }
    public String getHistory(int commandNumber) {
        if (commandNumber < 0 || commandNumber > commandHistory.size()) {
            return "";
        }
        return commandHistory.get(commandNumber);
    }

    public int getHistoryCount() {
        return commandHistory.size();
    }

    public void unregistercommand(Command command) {
        synchronized (commandData) {
            commandData.put(command.getName(), command);
        }
    }

    public void registerCommand(Command command) {
        synchronized (commandData) {
            commandData.put(command.getName(), command);
        }
    }

    public void pushHistory(String cmd) {
        commandHistory.add(0, cmd);
        if (commandHistory.size() >= commandHistoryCount) {
            commandHistory.remove(commandHistory.size() - 1);
        }
    }

    public void processCommand(String commandString) {
        if (echoCommand) {
            console.println(commandString);
        }
        pushHistory(commandString);

        if (commandString != null) {
            String splitCommand[] = commandString.split(";");
            for (String value : splitCommand) {
                String data = value.trim();
                if (echoSplitCommand) {
                	console.println(">" + data);
                }

                String cmd[] = data.split(" ", 2);
                String commandName = cmd[0];
                String argument = null;
                if (cmd.length > 1) {
                    argument = cmd[1];
                }
                try {
                    Command command = getCommand(commandName);
                    if (command != null) {
                        command.exec(argument);
                    } else {
                    	console.println("Unknown Command : " + data);
                        return;
                    }
                } catch (Throwable e) {
                	console.println("Error processing command " + e.getLocalizedMessage());
                	console.error(e);
                }

            }
        }

    }

    public Command getCommand(String name) {
        synchronized (commandData) {
            return commandData.get(name);
        }
    }

    public void showHelp() {
        synchronized (commandData) {
            int maxLength = 0;
            for (String name : commandData.keySet()) {
                if (name.length() > maxLength) {
                    maxLength = name.length();
                }
            }
            TreeMap<String, String> messages = new TreeMap<String,String>();
            for (String name : commandData.keySet()) {
                messages.put(name, String.format(" %" + maxLength + "s : %s", name, commandData.get(name).getDescription()));
            }
            for(String message : messages.values()){
            	console.println(message);
            }
        }
    }
}
