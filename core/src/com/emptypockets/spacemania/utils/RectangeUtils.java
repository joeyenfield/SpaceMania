package com.emptypockets.spacemania.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.network.old.engine.IntersectorUtils;

public class RectangeUtils {

	public static void ensurePositive(Rectangle bounds) {
		// TODO Auto-generated method stub
		if (bounds.width < 0) {
			bounds.x += bounds.width;
			bounds.width = -bounds.width;
		}

		if (bounds.height < 0) {
			bounds.y += bounds.height;
			bounds.height = -bounds.height;
		}
	}

	public static float xProgress(Rectangle region, float value) {
		return (value - region.x) / region.width;
	}

	public static void clampToScreen(Rectangle screen, Rectangle region, Rectangle result) {
		result.set(region);

		if (result.x < screen.x) {
			result.width -= screen.x - result.x;
			result.x = screen.x;
		}

		if (result.y < screen.y) {
			result.height -= screen.y - result.y;
			result.y = screen.y;
		}

		if (result.x + result.width > screen.x + screen.width) {
			result.width = screen.x + screen.width - result.x;
		}

		if (result.y + result.height > screen.y + screen.height) {
			result.height = screen.y + screen.height - result.y;
		}
	}

	public static boolean overlapsX(Rectangle recA, Rectangle recB) {
		return overlap(recA.x, recA.x + recA.width, recB.x, recB.x + recB.width);
	}

	public static boolean overlapsY(Rectangle recA, Rectangle recB) {
		return overlap(recA.y, recA.y + recA.height, recB.y, recB.y + recB.height);
	}

	public static boolean overlap(float r1Start, float r1End, float r2Start, float r2End) {
		if (r1Start > r2Start && r1Start < r2End)
			return true;
		if (r2Start > r1Start && r2Start < r1End)
			return true;
		return false;
	}

	public static boolean isSame(float v1, float v2, float delta) {
		return Math.abs(v1 - v2) < delta;
	}

	public static boolean same(Rectangle r1, Rectangle r2, float delta) {
		if (!isSame(r1.x, r2.x, delta)) {
			return false;
		}
		if (!isSame(r1.y, r2.y, delta)) {
			return false;
		}
		if (!isSame(r1.width, r2.width, delta)) {
			return false;
		}
		if (!isSame(r1.height, r2.height, delta)) {
			return false;
		}
		return true;
	}

	public static boolean inside(Rectangle region, Vector2 pos, float radius) {
		return IntersectorUtils.intersects(region, pos, radius);
	}

}
