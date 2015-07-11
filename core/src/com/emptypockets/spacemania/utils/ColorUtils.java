package com.emptypockets.spacemania.utils;

import com.badlogic.gdx.graphics.Color;

public class ColorUtils {

	public static Color HSVToColor(Color color, float h, float s, float v) {
		if (h == 0 && s == 0)
			return color.set(v, v, v, 1);

		float c = s * v;
		float x = c * (1 - Math.abs(h % 2 - 1));
		float m = v - c;

		if (h < 1)
			return color.set(c + m, x + m, m, 1);
		else if (h < 2)
			return color.set(x + m, c + m, m, 1);
		else if (h < 3)
			return color.set(m, c + m, x + m, 1);
		else if (h < 4)
			return color.set(m, x + m, c + m, 1);
		else if (h < 5)
			return color.set(x + m, m, c + m, 1);
		else
			return color.set(c + m, m, x + m, 1);
	}

}
