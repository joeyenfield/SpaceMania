package com.emptypockets.spacemania.metrics.plotter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.gui.tools.TextRender;
import com.emptypockets.spacemania.metrics.events.EventSystem;
import com.emptypockets.spacemania.metrics.events.render.EventRender;
import com.emptypockets.spacemania.utils.CameraHelper;
import com.emptypockets.spacemania.utils.PanAndZoomCamController;

public class EventViewer extends ApplicationAdapter {
	ShapeRenderer shape;
	OrthographicCamera camera;
	PanAndZoomCamController cameraControl;
	TextRender textRender;
	EventRender events;
	CameraHelper helper;

	@Override
	public void create() {
		helper = new CameraHelper();
		textRender = new TextRender();
		events = new EventRender();

		shape = new ShapeRenderer();
		camera = new OrthographicCamera();
		cameraControl = new PanAndZoomCamController(camera);
		cameraControl.setLimitZoom(false);
		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(cameraControl);

		Gdx.input.setInputProcessor(input);
		events.startTime = EventSystem.getRoot().getStartTime();
		events.endTime = events.startTime+3000;
		//EventSystem.start("OTHER");
	}

	public void render() {
		//EventSystem.stop("OTHER");
		
		//EventSystem.start("RENDER");

		//EventSystem.start("SETUP");
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		shape.setProjectionMatrix(camera.combined);
		//EventSystem.stop("SETUP");

		Rectangle bounds = new Rectangle();
		helper.getBounds(camera, bounds);

		events.drawScene(camera, shape, bounds);

		
		//EventSystem.stop("RENDER");
		//EventSystem.start("OTHER");
	}

	public static String randomString(int length) {
		StringBuilder builder = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			builder.append((char) MathUtils.random(Character.MAX_VALUE));
		}

		return builder.toString();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();

	}

}