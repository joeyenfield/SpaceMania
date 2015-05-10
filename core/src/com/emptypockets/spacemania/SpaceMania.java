package com.emptypockets.spacemania;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.BaseEntity;
import com.emptypockets.spacemania.engine.entityManager.BoundedEntityManager;
import com.emptypockets.spacemania.gui.ClientScreen;

public class SpaceMania extends Game {

	InputMultiplexer input;

	@Override
	public void create() {
		input = new InputMultiplexer();
		Gdx.input.setInputProcessor(input);
		Screen screen = new ClientScreen(input);
//		GridScreen screen = new GridScreen(input);
//		AudioMessageScreen screen = new AudioMessageScreen(input);
		setScreen(screen);
	}


//	@Override
//	public void render () {
//		Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		batch.draw(img, 0, 0,100,100);
//		batch.end();
//
//		manager.update(Gdx.graphics.getRawDeltaTime());
//		render.render(camera, manager);
//	}
//
//	@Override
//	public void resize(int width, int height) {
//		super.resize(width, height);
//		camera.viewportWidth = width;
//		camera.viewportHeight = height;
//		camera.position.set(width/2, height/2,0);
//		manager.setBounds(0,0,width,height);
//		camera.update();
//	}
}
