package com.emptypockets.spacemania.command.commands;

import com.emptypockets.spacemania.command.Command;
import com.emptypockets.spacemania.command.CommandLine;
import com.emptypockets.spacemania.console.Console;

public class CommandLineHelpCommand extends CommandLineCommand{

	public CommandLineHelpCommand(CommandLine commandHub) {
		super("help",commandHub);
		setDescription("Displays help for the system");
	}

	@Override
	public void exec(String data) {
		if(data == null || data.length() == 0){
			commandHub.showHelp();
		}else{
			Command command = commandHub.getCommand(data);
			if(command == null){
				Console.println("Unknown Command : "+data);
			}else{
				Console.println(command.getDescription());				
			}

		}
		
	}

}
