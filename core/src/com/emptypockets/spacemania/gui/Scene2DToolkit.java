package com.emptypockets.spacemania.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Scene2DToolkit {
	private static Scene2DToolkit toolkit;

	Skin skin;
	TextureAtlas atlas;
	FileHandle jsonFile;
	FileHandle atlasFile;

	private Scene2DToolkit() {

	}

	public static Scene2DToolkit getToolkit() {
		if (toolkit == null) {
			synchronized (Scene2DToolkit.class) {
				if (toolkit == null) {
					toolkit = new Scene2DToolkit();
				}
			}
		}
		return toolkit;
	}

	public void reloadSkin() {
		disposeSkin();
		getSkin();
	}

	public void disposeSkin() {
		if (skin != null) {
			skin.dispose();
			skin = null;
		}
	}

	public Skin getSkin() {
		if (skin == null) {
			synchronized (getClass()) {
				if (skin == null) {
					jsonFile = Gdx.files.internal("ui/uiskin.json");
					atlasFile = Gdx.files.internal("ui/uiskin.atlas");
					atlas = new TextureAtlas(atlasFile);
					skin = new Skin(jsonFile) {
						@Override
						public void dispose() {
							super.dispose();
							atlas.dispose();
						}
					};
				}
			}
		}
		return skin;
	}
}
