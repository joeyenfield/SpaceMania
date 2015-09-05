package com.emptypockets.spacemania.engine.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class KeyboardPlayerInputProducer extends PlayerInputProducer {

	int keyMoveUp = Keys.W;
	int keyMoveDown = Keys.S;
	int keyMoveLeft = Keys.A;
	int keyMoveRight = Keys.D;

	int keyShoot = Keys.SPACE;

	@Override
	public void update() {
		getPlayerInput().move.setZero();
		if (Gdx.input.isKeyPressed(keyMoveRight)) {
			getPlayerInput().move.x += +1;
		}
		if (Gdx.input.isKeyPressed(keyMoveLeft)) {
			getPlayerInput().move.x += -1;
		}

		if (Gdx.input.isKeyPressed(keyMoveUp)) {
			getPlayerInput().move.y += +1;
		}
		if (Gdx.input.isKeyPressed(keyMoveDown)) {
			getPlayerInput().move.y += -1;
		}

		if (Gdx.input.isKeyPressed(keyShoot)) {
			getPlayerInput().shoot = true;
		} else {
			getPlayerInput().shoot = false;
		}
	}
}
