package com.emptypockets.spacemania.engine.input;

public abstract class PlayerInputProducer {
	PlayerInputData playerInput = new PlayerInputData();

	public PlayerInputData getPlayerInput() {
		return playerInput;
	}

	public abstract void update();
}
