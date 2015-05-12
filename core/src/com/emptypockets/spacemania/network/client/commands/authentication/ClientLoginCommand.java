package com.emptypockets.spacemania.network.client.commands.authentication;


import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

public class ClientLoginCommand extends ClientCommand {

	public ClientLoginCommand(ClientManager client) {
		super("login", client);
		setDescription("Attempts to login to a remote server : login {username},{password}");
	}

	@Override
	public void exec(String data) {
		try {
			String[] args = data.split(",");
			String username = "";
			String password = "";
			username = args[0];
			if(args.length > 1){
				password = args[1];
			}
			client.serverLogin(args[0], args[1]);
		}catch(Throwable t){
			Console.printf("Invlaid Arguments");
			Console.error(t);
		}
	}
}
