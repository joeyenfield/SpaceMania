package com.emptypockets.spacemania.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class IntersectorUtils {
	public static boolean intersects(Rectangle bounds, Vector2 c, float radius){
		float closestX = c.x;
		float closestY = c.y;

		if (c.x < bounds.x) {
			closestX = bounds.x;
		} else if (c.x > bounds.x + bounds.width) {
			closestX = bounds.x + bounds.width;
		}

		if (c.y < bounds.y) {
			closestY = bounds.y;
		} else if (c.y > bounds.y + bounds.height) {
			closestY = bounds.y + bounds.height;
		}

		closestX = closestX - c.x;
		closestX *= closestX;
		closestY = closestY - c.y;
		closestY *= closestY;

		return closestX + closestY < radius * radius;
	}
}
