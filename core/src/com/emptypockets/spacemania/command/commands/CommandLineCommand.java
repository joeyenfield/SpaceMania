package com.emptypockets.spacemania.command.commands;


import com.emptypockets.spacemania.command.Command;
import com.emptypockets.spacemania.command.CommandLine;

public abstract class CommandLineCommand extends Command {

	CommandLine commandHub;
	
	public CommandLineCommand(String name, CommandLine commandHub) {
		super(name);
		this.commandHub = commandHub;
	}

}
