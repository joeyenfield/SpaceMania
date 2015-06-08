package com.emptypockets.spacemania.network.engine.grid.spring;

import com.badlogic.gdx.math.Vector2;

public abstract class NodeLink {
	public abstract void solve();
	public abstract void updateRestPos();
	public abstract Vector2 getP1();
	public abstract Vector2 getP2();
	public abstract float getForce();
}