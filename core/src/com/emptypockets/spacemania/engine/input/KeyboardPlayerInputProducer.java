package com.emptypockets.spacemania.engine.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class KeyboardPlayerInputProducer extends PlayerInputProducer {

	int keyMoveUp = Keys.W;
	int keyMoveDown = Keys.S;
	int keyMoveLeft = Keys.A;
	int keyMoveRight = Keys.D;

	int keyShootUp = Keys.UP;
	int keyShootDown = Keys.DOWN;
	int keyShootLeft = Keys.LEFT;
	int keyShootRight = Keys.RIGHT;

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

		getPlayerInput().shootDir.setZero();
		if (Gdx.input.isKeyPressed(keyShootRight)) {
			getPlayerInput().shootDir.x += +1;
		}
		if (Gdx.input.isKeyPressed(keyShootLeft)) {
			getPlayerInput().shootDir.x += -1;
		}

		if (Gdx.input.isKeyPressed(keyShootUp)) {
			getPlayerInput().shootDir.y += +1;
		}
		if (Gdx.input.isKeyPressed(keyShootDown)) {
			getPlayerInput().shootDir.y += -1;
		}

		getPlayerInput().move.limit2(1);
		getPlayerInput().shootDir.limit2(1);

		if (Gdx.input.isKeyPressed(keyShoot) || getPlayerInput().shootDir.len2() > 0.1f) {
			getPlayerInput().shoot = true;
		} else {
			getPlayerInput().shoot = false;
		}
	}
}
