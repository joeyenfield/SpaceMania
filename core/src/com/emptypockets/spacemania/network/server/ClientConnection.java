package com.emptypockets.spacemania.network.server;

import com.esotericsoftware.kryonet.Connection;

public class ClientConnection extends Connection {
	String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
