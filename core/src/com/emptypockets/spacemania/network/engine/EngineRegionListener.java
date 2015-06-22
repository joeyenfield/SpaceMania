package com.emptypockets.spacemania.network.engine;

import com.badlogic.gdx.math.Rectangle;

public interface EngineRegionListener {
	public void notifyRegionChanged(Rectangle region);
}
