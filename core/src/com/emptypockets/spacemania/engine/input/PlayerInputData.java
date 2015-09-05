package com.emptypockets.spacemania.engine.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlayerInputData implements Poolable {
	public Vector2 move = new Vector2();
	public boolean shoot = false;

	@Override
	public void reset() {
		move.setZero();
		shoot = false;
	}

}
