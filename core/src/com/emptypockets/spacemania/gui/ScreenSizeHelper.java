package com.emptypockets.spacemania.gui;

import com.badlogic.gdx.Gdx;

public class ScreenSizeHelper {
	public static int getcmtoPxlX(float cm){
		return Math.round(cm*Gdx.graphics.getPpcX());
	}
	
public static int getcmtoPxlY(float cm){
		return Math.round(cm*Gdx.graphics.getPpcY());
	}
}
