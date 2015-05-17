package com.emptypockets.spacemania.commandLine.commands;


import com.emptypockets.spacemania.commandLine.Command;
import com.emptypockets.spacemania.commandLine.CommandLine;
import com.emptypockets.spacemania.console.Console;

public abstract class CommandLineCommand extends Command {

    CommandLine commandHub;

    public CommandLineCommand(String name, CommandLine commandHub) {
        super(name);
        this.commandHub = commandHub;
    }

	public CommandLine getCommandHub() {
		return commandHub;
	}
    
    public Console getConsole(){
    	return commandHub.getConsole();
    }

}
