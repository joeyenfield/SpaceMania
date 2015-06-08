package com.emptypockets.spacemania.network.engine.entities.behaviour;

import com.emptypockets.spacemania.network.engine.Engine;
import com.emptypockets.spacemania.network.engine.entities.Entity;

public abstract class Behaviour {
	public abstract void update(Engine engine);
	public abstract void apply(Entity entity);
}
