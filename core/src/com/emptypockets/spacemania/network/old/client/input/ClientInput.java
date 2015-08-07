package com.emptypockets.spacemania.network.old.client.input;

import com.badlogic.gdx.math.Vector2;

public class ClientInput {
	Vector2 move;
	Vector2 shoot;

	public ClientInput() {
		move = new Vector2();
		shoot = new Vector2();
	}

	public Vector2 getMove() {
		return move;
	}

	public Vector2 getShoot() {
		return shoot;
	}

	public void read(ClientInput input) {
		move.set(input.move);
		shoot.set(input.shoot);
	}
}
