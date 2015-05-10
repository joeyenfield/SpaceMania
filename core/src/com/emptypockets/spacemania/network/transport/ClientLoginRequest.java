package com.emptypockets.spacemania.network.transport;

public class ClientLoginRequest {
	String username;

	public ClientLoginRequest() {
	}

	public ClientLoginRequest(String name) {
		setUsername(name);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
