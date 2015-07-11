package com.emptypockets.spacemania.commandLine.commands;

import com.emptypockets.spacemania.commandLine.Command;
import com.emptypockets.spacemania.commandLine.CommandLine;

public class CommandLineHelpCommand extends CommandLineCommand {

    public CommandLineHelpCommand(CommandLine commandHub) {
        super("help", commandHub);
        setDescription("Displays help for the system");
    }

    @Override
    public void exec(String data) {
        if (data == null || data.length() == 0) {
            commandHub.showHelp();
        } else {
            Command command = commandHub.getCommand(data);
            if (command == null) {
                getConsole().println("Unknown Command : " + data);
            } else {
            	getConsole().println(command.getDescription());
            }

        }

    }

}
