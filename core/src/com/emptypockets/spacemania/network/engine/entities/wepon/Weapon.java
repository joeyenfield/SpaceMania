package com.emptypockets.spacemania.network.engine.entities.wepon;

import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.engine.entities.Entity;

public abstract class Weapon {
	Entity owner;
	
	Vector2 pos;
	Vector2 dir;
	
	public abstract void shoot();
	
	
	public Vector2 getPos() {
		return pos;
	}
	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
	public Vector2 getDir() {
		return dir;
	}
	public void setDir(Vector2 dir) {
		this.dir = dir;
	}
	
	
}
