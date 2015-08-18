package com.emptypockets.spacemania.network.old.client.commands.connection;

import com.emptypockets.spacemania.network.common.utils.IpManager;
import com.emptypockets.spacemania.network.old.client.ClientManager;
import com.emptypockets.spacemania.network.old.client.commands.ClientCommand;

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
