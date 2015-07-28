package com.emptypockets.spacemania.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetStore {
	TextureAtlas textureAtlas;

	public AssetStore(){
		textureAtlas = new TextureAtlas("game/game.atlas");
		for (Texture t : textureAtlas.getTextures()) {
			t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
	}
	public TextureRegion getRegion(String name) {
		return textureAtlas.findRegion("playership");
	}
}
