package com.emptypockets.spacemania.commandLine.commands;


import com.emptypockets.spacemania.commandLine.Command;
import com.emptypockets.spacemania.commandLine.CommandLine;

public abstract class CommandLineCommand extends Command {

    CommandLine commandHub;

    public CommandLineCommand(String name, CommandLine commandHub) {
        super(name);
        this.commandHub = commandHub;
    }

}
