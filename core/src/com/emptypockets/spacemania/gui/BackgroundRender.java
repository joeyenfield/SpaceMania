package com.emptypockets.spacemania.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.gui.tools.TextRender;

public class BackgroundRender {
	Texture starfieldDeepTexture;
	Texture starfieldParalaxTexture;
	Rectangle screenView = new Rectangle();
	Vector2 uvMap1 = new Vector2();
	Vector2 uvMap2 = new Vector2();
	
	float deepImgScale = 2;
	float deepMovementScale = 0.05f;
	
	float[] imgScale = new float[]{1,1};
	float[] movementScale = new float[]{0.5f,1};
	float[] offsetX = new float[]{0,0.5f};
	float[] offsetY = new float[]{0,0.5f};
	
	boolean basic = true;
	
	public BackgroundRender() {
		super();
		createImages();
	}

	private void createImages() {
		starfieldDeepTexture = new Texture("background.png");
		starfieldParalaxTexture = new Texture("starfield-b-2048.png");

		starfieldDeepTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		starfieldParalaxTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		starfieldDeepTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		starfieldParalaxTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	public void render(GameEngine engine, Rectangle screen, ShapeRenderer shapeBatch, SpriteBatch spriteBatch, TextRender textHelper, float pixelSize) {
		screenView.set(screen);

		Vector2 offset = engine.worldRenderOffset;
		screenView.x -= offset.x;
		screenView.y -= offset.y;

		spriteBatch.begin();
		spriteBatch.disableBlending();
		spriteBatch.setColor(1, 1, 1, 1);

		getUvMap(screen, starfieldDeepTexture, deepMovementScale, deepImgScale,0,0, uvMap1, uvMap2);
		spriteBatch.draw(starfieldDeepTexture, screen.x, screen.y, screen.width, screen.height, uvMap1.x,uvMap1.y, uvMap2.x,uvMap2.y);
		
		spriteBatch.enableBlending();
		for(int i = 0; i < imgScale.length; i++){
			getUvMap(screen, starfieldParalaxTexture, movementScale[i],imgScale[i],offsetX[i],offsetY[i],uvMap1, uvMap2);
			spriteBatch.draw(starfieldParalaxTexture, screen.x, screen.y, screen.width, screen.height, uvMap1.x,uvMap1.y, uvMap2.x,uvMap2.y);
		}
		spriteBatch.end();
	}

	private void getUvMap(Rectangle screen, Texture image, float movementScale, float imageScale,float offsetU, float offsetV,Vector2 uvMap1, Vector2 uvMap2 ) {
		uvMap1.x = offsetU+(screen.x*movementScale)/(imageScale*image.getWidth());
		uvMap1.y = offsetV+(screen.y*movementScale)/(imageScale*image.getHeight());
		uvMap2.x = offsetU+(screen.x*movementScale+screen.width)/(imageScale*image.getWidth());
		uvMap2.y = offsetV+(screen.y*movementScale+screen.height)/(imageScale*image.getHeight());
		
		float count = 4;
		if(uvMap1.dst2(uvMap2) > count*count){
			float dist = uvMap1.dst(uvMap2); 

			uvMap2.lerp(uvMap1, 1-count/dist);
		}
	}
}
