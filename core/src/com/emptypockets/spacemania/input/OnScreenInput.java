package com.emptypockets.spacemania.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public class OnScreenInput extends ClientInputProducer {
	Touchpad movePad;
	Touchpad shootPad;

	int keyMoveUp = Keys.W;
	int keyMoveDown = Keys.S;
	int keyMoveLeft = Keys.A;
	int keyMoveRight = Keys.D;

	int keyShootUp = Keys.UP;
	int keyShootDown = Keys.DOWN;
	int keyShootLeft = Keys.LEFT;
	int keyShootRight = Keys.RIGHT;

	@Override
	public void update() {
		getInput().getMove().set(0, 0);
		getInput().getShoot().set(0, 0);

		if (movePad != null) {
			getInput().getMove().set(movePad.getKnobPercentX(), movePad.getKnobPercentY());
		}
		if (shootPad != null) {
			getInput().getShoot().set(shootPad.getKnobPercentX(), shootPad.getKnobPercentY());
		}

		if (Gdx.input.isKeyPressed(keyMoveRight)) {
			getInput().getMove().x += +1;
		}
		if (Gdx.input.isKeyPressed(keyMoveLeft)) {
			getInput().getMove().x += -1;
		}

		if (Gdx.input.isKeyPressed(keyMoveUp)) {
			getInput().getMove().y += +1;
		}
		if (Gdx.input.isKeyPressed(keyMoveDown)) {
			getInput().getMove().y += -1;
		}

		if (Gdx.input.isKeyPressed(keyShootRight)) {
			getInput().getShoot().x += +1;
		}
		if (Gdx.input.isKeyPressed(keyShootLeft)) {
			getInput().getShoot().x += -1;
		}

		if (Gdx.input.isKeyPressed(keyShootUp)) {
			getInput().getShoot().y += +1;
		}
		if (Gdx.input.isKeyPressed(keyShootDown)) {
			getInput().getShoot().y += -1;
		}
	}

	public Touchpad getMovePad() {
		return movePad;
	}

	public void setMovePad(Touchpad movePad) {
		this.movePad = movePad;
	}

	public Touchpad getShootPad() {
		return shootPad;
	}

	public void setShootPad(Touchpad shootPad) {
		this.shootPad = shootPad;
	}

}
