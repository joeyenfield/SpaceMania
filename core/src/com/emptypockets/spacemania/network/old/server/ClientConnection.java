package com.emptypockets.spacemania.network.old.server;

import java.util.ArrayList;

import com.emptypockets.spacemania.network.old.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.old.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.old.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.transport.ComsType;
import com.emptypockets.spacemania.utils.KryoUtils;
import com.esotericsoftware.kryonet.Connection;

public class ClientConnection extends Connection {
	ServerManager serverManager;
	ServerPlayer player;
	private boolean loggedIn;

	ArrayList<ServerPayload> incommingPayloads;

	static Boolean metrics = true;
	static int packetCount= 0;
	static float dataRate = 0;
	static long dataSize = 0;
	static long lastUpdate = 0;
	static long desiredUpdate = 1000;

	public ClientConnection(ServerManager serverManager) {
		super();
		this.serverManager = serverManager;
		incommingPayloads = new ArrayList<ServerPayload>();
	}

	public void send(ClientPayload payload, ComsType type) {
		synchronized (metrics) {

			if (metrics) {
				packetCount++;
				dataSize += KryoUtils.getSize(payload);
				long delta = System.currentTimeMillis() - lastUpdate;
				if (delta > desiredUpdate) {
					lastUpdate = System.currentTimeMillis();
					float deltaSec = delta/1000f;
					dataRate = (dataSize/(float)deltaSec);
					System.out.println("DATA (ClientConnection.java:43):"+packetCount+" - "+dataRate);
					dataSize = 0;
					packetCount = 0;
				}
			}
		}
		try {
			if (type == ComsType.TCP) {
				sendTCP(payload);
			} else {
				sendUDP(payload);
			}
		} catch (Throwable t) {
			serverManager.console.println("Error sending payload");
			serverManager.console.error(t);
		}
	}

	public void recieve(ServerPayload payload) {
		synchronized (incommingPayloads) {
			incommingPayloads.add(payload);
		}
	}

	public void processIncommingPayloads() {
		synchronized (incommingPayloads) {
			int size = incommingPayloads.size();
			for (int i = 0; i < size; i++) {
				ServerPayload payload = incommingPayloads.get(i);
				payload.executePayload(this, serverManager);
			}
			incommingPayloads.clear();
		}
	}

	public boolean getLoggedIn() {
		return loggedIn;
	}

	public ServerPlayer getPlayer() {
		return player;
	}

	public void setPlayer(ServerPlayer player) {
		this.player = player;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
}
