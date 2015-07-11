package com.emptypockets.spacemania.network.client.commands.connection;

import com.emptypockets.spacemania.network.IpManager;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.commands.ClientCommand;

public class ClientIpCommand extends ClientCommand {

	public ClientIpCommand(ClientManager client) {
		super("ip", client);
		setDescription("Lists all ips of this server");
	}

	@Override
	public void exec(String data) {
		for(String ip : IpManager.getIP()){
			client.getConsole().println(ip);
		}
	}
}
