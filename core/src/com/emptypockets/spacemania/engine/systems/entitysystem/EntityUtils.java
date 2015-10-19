package com.emptypockets.spacemania.engine.systems.entitysystem;

import com.badlogic.gdx.math.Rectangle;

public class EntityUtils {

	public static boolean isEntityInRegion(GameEntity entity, Rectangle region) {
		if (region.contains(entity.linearTransform.state.pos)) {
			return true;
		}
		return false;
	}
}
