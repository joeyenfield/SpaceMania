package com.emptypockets.spacemania.gui.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.emptypockets.spacemania.utils.event.EventRecorder;

public abstract class GameScreen implements Screen, GestureListener, InputProcessor {

	protected OrthographicCamera screenCamera = new OrthographicCamera();
	
	GestureDetector gesture;
	protected Color clearColor = new Color(1, 1, 1, 1);
	protected Skin skin;
	protected EventRecorder eventLogger;
	SpriteBatch eventBatch;
	protected OrthographicCamera eventCamera = new OrthographicCamera();
	
	InputMultiplexer parentInputMultiplexer;

	boolean drawEvents = false;
	
	public GameScreen(InputMultiplexer inputProcessor) {
		this.parentInputMultiplexer = inputProcessor;
		this.gesture = new GestureDetector(this);
		eventLogger = new EventRecorder(500);
	}

	public void initializeRender() {
		screenCamera.update();
		eventCamera.update();

		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); // #14
	}

	protected void addInputMultiplexer(InputMultiplexer input) {
		input.addProcessor(this);
		input.addProcessor(gesture);
	}

	public Skin getSkin() {
		return Scene2DToolkit.getToolkit().getSkin();
	}

	public void removeInputMultiplexer(InputMultiplexer input) {
		input.removeProcessor(this);
		input.removeProcessor(gesture);
	}

	public GestureDetector getGesture() {
		return gesture;
	}

	public void setGesture(GestureDetector gesture) {
		this.gesture = gesture;
	}

	@Override
	public final void render(float delta) {
		eventLogger.begin("LOGIC");
		updateLogic(delta);
		eventLogger.end("LOGIC");

		eventLogger.begin("RENDER");
		initializeRender();
		renderScreen(delta);
		eventLogger.end("RENDER");
	}

	public void renderScreen(float delta) {
		eventLogger.begin("RENDER-Background");
		drawBackground(delta);
		eventLogger.end("RENDER-Background");

		eventLogger.begin("RENDER-Screen");
		drawScreen(delta);
		eventLogger.end("RENDER-Screen");

		eventLogger.begin("RENDER-Overlay");
		drawOverlay(delta);
		eventLogger.end("RENDER-Overlay");
		
		
		if(drawEvents){
			eventLogger.begin("RENDER-Event Overlay");
			drawEventOverlay(delta);
			eventLogger.end("RENDER-Event Overlay");
			
			
		}
		
	}

	public void drawEventOverlay(float delta){
		if(eventBatch == null){
			eventBatch = new SpriteBatch();
		}
		eventBatch.setProjectionMatrix(eventCamera.combined);
		BitmapFont font = getSkin().getFont("default-font");
		
		eventBatch.begin();
		eventLogger.draw(eventBatch, font, -Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2, 20);
		font.draw(eventBatch, "FPS : " + Gdx.graphics.getFramesPerSecond(), 0, -Gdx.graphics.getHeight() / 2 + 50);
		eventBatch.end();
		
	}
	public void updateLogic(float delta) {
	}

	public void pause() {
	}

	public void resume() {

	}

	public abstract void drawBackground(float delta);

	public abstract void drawScreen(float delta);

	public abstract void drawOverlay(float delta);

	@Override
	public void resize(int width, int height) {
		screenCamera.viewportWidth = width;
		screenCamera.viewportHeight = height;
		screenCamera.position.x = width/2;
		screenCamera.position.y=height/2;
		eventCamera.viewportWidth = width;
		eventCamera.viewportHeight = height;
	}

	@Override
	public void show() {
		Scene2DToolkit.getToolkit().reloadSkin();
		addInputMultiplexer(parentInputMultiplexer);
	}

	@Override
	public void hide() {
		removeInputMultiplexer(parentInputMultiplexer);
		Scene2DToolkit.getToolkit().disposeSkin();
		if(eventBatch != null){
			eventBatch.dispose();
			eventBatch = null;
		}
	}

	@Override
	public void dispose() {
	}

	public Color getClearColor() {
		return clearColor;
	}

	public void setClearColor(Color clearColor) {
		this.clearColor = clearColor;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	public OrthographicCamera getScreenCamera() {
		return screenCamera;
	}

	public void setDrawEvents(boolean drawEvents) {
		this.drawEvents = drawEvents;
		eventLogger.setEnabled(drawEvents);
	}

}
