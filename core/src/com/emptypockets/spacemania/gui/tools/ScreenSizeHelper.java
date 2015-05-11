package com.emptypockets.spacemania.gui.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScreenSizeHelper {
	public static int getcmtoPxlX(float cm){
		return Math.round(cm*Gdx.graphics.getPpcX());
	}
	
public static int getcmtoPxlY(float cm){
		return Math.round(cm*Gdx.graphics.getPpcY());
	}
}
