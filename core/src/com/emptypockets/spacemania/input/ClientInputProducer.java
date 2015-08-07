package com.emptypockets.spacemania.input;

import com.emptypockets.spacemania.network.old.client.input.ClientInput;

public class ClientInputProducer {
	ClientInput input;

	public ClientInputProducer() {
		input = new ClientInput();
	}
	
	public ClientInput getInput(){
		return input;
	}
	
	public void update(){
	}
}
