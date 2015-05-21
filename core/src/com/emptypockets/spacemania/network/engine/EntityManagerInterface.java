package com.emptypockets.spacemania.network.engine;

import com.emptypockets.spacemania.network.engine.entities.Entity;

public interface EntityManagerInterface {
	public void entityAdded(Entity entity);
	public void entityRemoved(Entity entity);
}
