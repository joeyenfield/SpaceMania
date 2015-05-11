package com.emptypockets.spacemania.network.server;

import com.esotericsoftware.kryonet.Connection;

public class ClientConnection extends Connection {
	String username;
	private boolean loggedIn;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean getLoggedIn() {
		return loggedIn;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
}
