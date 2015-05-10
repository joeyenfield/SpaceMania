package com.emptypockets.spacemania.network.transport;

public class ClientLogoutRequest {
	String username;

	public ClientLogoutRequest() {
	}

	public ClientLogoutRequest(String name) {
		setUsername(name);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
