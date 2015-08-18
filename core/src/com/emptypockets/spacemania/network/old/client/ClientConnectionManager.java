package com.emptypockets.spacemania.network.old.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.network.common.ComsType;
import com.emptypockets.spacemania.network.common.NetworkProtocall;
import com.emptypockets.spacemania.network.old.client.exceptions.ClientNotConnectedException;
import com.emptypockets.spacemania.network.old.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.old.server.payloads.ServerPayload;
import com.emptypockets.spacemania.utils.PoolsManager;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Created by jenfield on 12/05/2015.
 */
public class ClientConnectionManager extends Listener implements Disposable {
	ArrayList<ClientPayload> recievedPayloads;
	ArrayList<ServerPayload> toSendPayloadsUdp;
	ArrayList<ServerPayload> toSendPayloadsTcp;

	Object clientConnectionLock = new Object();
	Client connection;
	ClientManager manager;

	public ClientConnectionManager(ClientManager manager) {
		this.manager = manager;
		recievedPayloads = new ArrayList<ClientPayload>();
		toSendPayloadsUdp = new ArrayList<ServerPayload>();
		toSendPayloadsTcp = new ArrayList<ServerPayload>();
	}

	public static void listNetworkServers(final int udpPort, final int timeoutSec, final NetworkDiscoveryInterface callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Client connection = new Client();

				// TODO Auto-generated method stub
				List<InetAddress> hosts = connection.discoverHosts(udpPort, timeoutSec * 1000);
				if (callback != null) {
					callback.notifyDiscoveredHosts(hosts);
				}
			}
		}).start();
	}

	private void setupClientConnection() {
		synchronized (clientConnectionLock) {
			disconnect();
			connection = new Client(Constants.CLIENT_BUFFER_WRITE, Constants.CLIENT_BUFFER_OBJECT);
			connection.start();
			connection.addListener(this);
			NetworkProtocall.register(connection.getKryo());
		}
	}

	public void connect(String address, int tcpPort, int udpPort) throws IOException {
		manager.getConsole().printf("Connecting to server %s : %d,%d\n", address, tcpPort, udpPort);
		synchronized (clientConnectionLock) {
			setupClientConnection();
			connection.connect(20000, address, tcpPort, udpPort);
		}
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		if (object instanceof ClientPayload) {
			addPayload((ClientPayload) object);
		}
	}

	public boolean isConnected() {
		synchronized (clientConnectionLock) {
			if (connection != null && connection.isConnected()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void idle(Connection connection) {
		super.idle(connection);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		disconnect();
	}

	public void disconnect() {
		processToSendPayloads();
		synchronized (clientConnectionLock) {
			if (connection != null && connection.isConnected()) {
				connection.close();
				connection.stop();
				connection = null;
			}
		}
		manager.postDisconnect();
	}

	public void listStatus() {
		synchronized (clientConnectionLock) {
			if (connection == null) {
				manager.getConsole().println("Connection : Connected[false] : Ping[]");
			} else {
				manager.getConsole().println("Connection : Connected[" + connection.isConnected() + "] : Ping[" + getPing() + "]");
			}
		}
	}

	public void send(ServerPayload payload, ComsType type) {
		if (type == ComsType.TCP) {
			synchronized (toSendPayloadsTcp) {
				toSendPayloadsTcp.add(payload);
			}
		} else {
			synchronized (toSendPayloadsUdp) {
				toSendPayloadsUdp.add(payload);
			}
		}
	}

	private void sendTCP(Object data) {
		synchronized (clientConnectionLock) {
			// Send Current State to server
			if (connection != null && connection.isConnected()) {
				connection.sendTCP(data);
			} else {
				throw new ClientNotConnectedException();
			}
		}
	}

	private void sendUDP(Object data) {
		synchronized (clientConnectionLock) {
			// Send Current State to server
			if (connection != null && connection.isConnected()) {
				connection.sendUDP(data);
			} else {
				throw new ClientNotConnectedException();
			}
		}
	}

	public void updatePing() {
		synchronized (clientConnectionLock) {
			if (connection != null && connection.isConnected()) {
				connection.updateReturnTripTime();
			}
		}
	}

	public int getPing() {
		synchronized (clientConnectionLock) {
			if (connection != null && connection.isConnected()) {
				return connection.getReturnTripTime();
			}
		}
		return -1;
	}

	public void addPayload(ClientPayload object) {
		synchronized (recievedPayloads) {
			recievedPayloads.add(object);
		}
	}

	public void processRecievedPayloads() {
		// Process Payloads
		synchronized (recievedPayloads) {
			for (ClientPayload payload : recievedPayloads) {
				payload.executePayload(manager);
			}
			recievedPayloads.clear();
		}
	}

	public void processToSendPayloads() {
		// Send Coms to Server
		synchronized (toSendPayloadsTcp) {
			for (ServerPayload payload : toSendPayloadsTcp) {
				try {
					sendTCP(payload);
					PoolsManager.free(payload);
				} catch (Throwable t) {
					manager.getConsole().println("Failed to send packet");
					manager.getConsole().error(t);
				}
			}
			toSendPayloadsTcp.clear();
		}

		synchronized (toSendPayloadsUdp) {
			for (ServerPayload payload : toSendPayloadsUdp) {
				try {
					sendUDP(payload);
					PoolsManager.free(payload);
				} catch (Throwable t) {
					manager.getConsole().println("Failed to send packet");
					manager.getConsole().error(t);
				}
			}
			toSendPayloadsUdp.clear();
		}
	}

	public void dispose() {
		disconnect();
		synchronized (toSendPayloadsUdp) {
			toSendPayloadsUdp.clear();
		}
		synchronized (toSendPayloadsTcp) {
			toSendPayloadsTcp.clear();
		}
		synchronized (recievedPayloads) {
			recievedPayloads.clear();
		}
		;
	}
}
