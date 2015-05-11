package com.emptypockets.spacemania.network.client.commands;

import com.emptypockets.spacemania.network.client.ClientManager;

public class ClientServerCommand extends ClientCommand {

	public ClientServerCommand(ClientManager client) {
		super("server", client);
		setDescription("This issues commands to the clients local server (see server help) : server [arg] ");
	}

	@Override
	public void exec(String data) {
		if(data != null && data.startsWith("setup")){
			String arg[] = data.split(" ");
			int count = 10;
			if(arg.length > 1){
				count = Integer.parseInt(arg[1]);
			}
			client.setupServer(count);
		}else{
			client.getServerManager().getCommand().processCommand(data);
		}
	}

}
