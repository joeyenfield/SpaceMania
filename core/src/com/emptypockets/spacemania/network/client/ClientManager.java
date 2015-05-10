package com.emptypockets.spacemania.network.client;

import com.emptypockets.spacemania.command.CommandLine;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.CommandService;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.transport.ClientLoginRequest;
import com.emptypockets.spacemania.network.transport.ClientLogoutRequest;
import com.emptypockets.spacemania.network.transport.ClientStateTransferObject;
import com.emptypockets.spacemania.network.transport.LoginFailedResponse;
import com.emptypockets.spacemania.network.transport.LoginSucessfulResponse;
import com.emptypockets.spacemania.network.transport.NetworkProtocall;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;


public class ClientManager extends Listener {

	Client client;
	Object engineLock = new Object();

	private CommandLine command;
	String username = "client";
	ServerManager serverManager;
	boolean loggedIn = false;

	public ClientManager() {
		setCommand(new CommandLine());
		setupClient();
		NetworkProtocall.register(client.getKryo());
		CommandService.registerClient(this);
	}

	public void setupClient() {
		client = new Client();
		client.start();
		client.addListener(this);
	}

	public void setupServer(int updateCount) {
		serverManager = new ServerManager(updateCount);
	}

	public ServerManager getServerManager() {
		if (serverManager == null) {
			serverManager = new ServerManager();
		}
		return serverManager;
	}

	public void connect(String address, int tcpPort, int udpPort) throws IOException {
		loggedIn = false;
		Console.printf("Connecting to server %s : %d,%d\n", address, tcpPort, udpPort);
		client.connect(20000, address, tcpPort, udpPort);
	}

	public void listStatus() {
		Console.println("Connected : " + client.isConnected() + " - LoggedIn : " + loggedIn);
	}

	public void listNetworkServers(final int udpPort, final int timeoutSec, final NetworkDiscoveryInterface callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Console.println("Searching for hosts Port [" + udpPort + "] holding for [" + timeoutSec + " s]");
				List<InetAddress> hosts = client.discoverHosts(udpPort, timeoutSec * 1000);
				Console.println("Found : " + hosts.size());
				for (InetAddress host : hosts) {
					Console.println("Host : " + host.getHostAddress() + " - " + host.getHostName());
				}
				if (callback != null) {
					callback.notifyDiscoveredHosts(hosts);
				}
			}
		}).start();
	}

	public void stop() {
		Console.println("Disconnecting from server");
		client.close();
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		if (object instanceof LoginSucessfulResponse) {
			loginSucessfull((LoginSucessfulResponse) object);
		} else if (object instanceof LoginFailedResponse) {
			loginFailed((LoginFailedResponse) object);
		}
	}

	public CommandLine getCommand() {
		return command;
	}

	public void setCommand(CommandLine command) {
		this.command = command;
	}

	public void setUsername(String data) {
		this.username = data;
	}

	public void serverLogin() {
		Console.println("Sending Login Request to server");
		client.sendTCP(new ClientLoginRequest(username));
	}

	public void serverLogout() {
		Console.println("Sending Logout Request to server");
		client.sendTCP(new ClientLogoutRequest(username));
	}

	public void loginSucessfull(LoginSucessfulResponse sucessfull) {
		Console.println("Logout Response : Sucessfully Logged in.");
		loggedIn = true;
	}

	public void loginFailed(LoginFailedResponse failed) {
		Console.println("Logout Response : Login FAILED - " + failed.getRejectionReason());
		loggedIn = false;
	}

	public void send(ClientStateTransferObject state) {
		if (client.isConnected() && loggedIn) {
			state.username = username;
			client.sendUDP(state);
		}
	}

	public String getUsername() {
		return username;
	}
}
