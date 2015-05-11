package com.emptypockets.spacemania.network.client.commands;

import java.net.InetAddress;
import java.util.List;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.NetworkProperties;
import com.emptypockets.spacemania.network.client.ClientManager;
import com.emptypockets.spacemania.network.client.NetworkDiscoveryInterface;

public class ClientSearchCommand extends ClientCommand {

	public ClientSearchCommand(ClientManager client) {
		super("search", client);
		setDescription("Searches for servers on the network : search (tcpPort,udpPort,timeoutSec)");
	}

	@Override
	public void exec(String data) {
		int tcpPort = NetworkProperties.tcpPort;
		int udpPort = NetworkProperties.udpPort; 
		int timeoutSec = NetworkProperties.discoveryTimeoutSec;
		if(data != null && data.length() != 0){
			String arg[] = data.split(" ");
			tcpPort = Integer.parseInt(arg[0]);
			udpPort = Integer.parseInt(arg[1]);
			timeoutSec = Integer.parseInt(arg[2]);
		}
		client.listNetworkServers(udpPort, timeoutSec, new PushHostsToConsoleCallback(this, tcpPort, udpPort));
	}
}

class PushHostsToConsoleCallback implements NetworkDiscoveryInterface {
	
	ClientSearchCommand command;
	int tcpPort;
	int udpPort;
	public PushHostsToConsoleCallback(ClientSearchCommand command, int tcpPort, int udpPort){
		this.command = command;
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
	}
	
	@Override
	public void notifyDiscoveredHosts(List<InetAddress> hosts) {
		for (InetAddress host : hosts) {
			Console.println("Host : " + host.getHostAddress() +" - "+host.getHostName());
			command.client.getCommand().pushHistory("connect "+host.getHostAddress()+","+tcpPort+","+udpPort);
		}

	}

}
