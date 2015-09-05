package com.emptypockets.spacemania.network.old.input;

import com.emptypockets.spacemania.network.old.client.input.ClientInput;

public abstract class ClientInputProducer {
	ClientInput input;

	public ClientInputProducer() {
		input = new ClientInput();
	}

	public ClientInput getInput() {
		return input;
	}

	public abstract void update();
}
