package com.emptypockets.spacemania.command;

import java.util.HashMap;

import com.emptypockets.spacemania.command.commands.CommandLineHelpCommand;
import com.emptypockets.spacemania.console.Console;

public class CommandLine {
	HashMap<String, Command> commands;
	CommandLinePanel panel;

	public CommandLine() {
		commands = new HashMap<String, Command>();
		registerCommand(new CommandLineHelpCommand(this));
	}

	public void unregistercommand(Command command) {
		synchronized (commands) {
			commands.put(command.getName(), command);
		}
	}

	public void registerCommand(Command command) {
		synchronized (commands) {
			commands.put(command.getName(), command);
		}
	}

	public CommandLinePanel getPanel() {
		if (panel == null) {
			panel = new CommandLinePanel(this);
		}
		return panel;
	}

	public void processCommand(String data) {
		Console.println(data);
		boolean commandFound = false;

		if (data != null) {
			String cmd[] = data.split(" ", 2);
			String commandName = cmd[0];
			String argument = null;
			if (cmd.length > 1) {
				argument = cmd[1];
			}
			try {
				Command command = getCommand(commandName);
				if (command != null) {
					commandFound = true;
					command.exec(argument);
				}
			} catch (Throwable e) {
				Console.println("Error processing command " + e.getLocalizedMessage());
			}
		}
		if (commandFound == false) {
			Console.println("Unknown Command : " + data);
		}
	}

	public Command getCommand(String name) {
		synchronized (commands) {
			return commands.get(name);
		}
	}

	public void showHelp() {
		synchronized (commands) {
			int maxLength = 0;
			for (String name : commands.keySet()) {
				if (name.length() > maxLength) {
					maxLength = name.length();
				}
			}
			for (String name : commands.keySet()) {
				Console.printf(" %" + maxLength + "s : %s\n", name, commands.get(name).getDescription());
			}
		}
	}
}
