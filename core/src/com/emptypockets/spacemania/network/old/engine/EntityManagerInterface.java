package com.emptypockets.spacemania.network.old.engine;

import com.emptypockets.spacemania.network.old.engine.entities.Entity;

public interface EntityManagerInterface {
	public void entityAdded(Entity entity);
	public void entityRemoved(Entity entity, boolean killed);
}
