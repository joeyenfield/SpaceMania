package com.emptypockets.spacemania.gui.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.utils.OrthoCamController;
import com.emptypockets.spacemania.utils.event.EventRecorder;

public abstract class GameScreen implements Screen, GestureListener, InputProcessor {
	protected Color clearColor = new Color(1, 1, 1, 1);

	protected MainGame mainGame;

	Viewport overlayViewport;

	protected OrthographicCamera screenCamera;
	protected OrthographicCamera overlayCamera;
    protected OrthoCamController control;

	Stage stage;
	GestureDetector gesture;

	protected EventRecorder eventLogger;
	SpriteBatch eventBatch;
	InputMultiplexer parentInputMultiplexer;
	boolean drawEvents = false;

	public GameScreen(MainGame game, InputMultiplexer inputProcessor) {
		this.mainGame = game;
		this.parentInputMultiplexer = inputProcessor;
		this.gesture = new GestureDetector(this);
		eventLogger = new EventRecorder(500);
	}

	public void initializeRender() {
		screenCamera.update();
		overlayCamera.update();

		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); // #14
	}

	protected void addInputMultiplexer(InputMultiplexer input) {
		input.addProcessor(this);
		input.addProcessor(gesture);
		input.addProcessor(control);
	}

	public void removeInputMultiplexer(InputMultiplexer input) {
		input.removeProcessor(this);
		input.removeProcessor(gesture);
		input.removeProcessor(control);
		
	}

	public Skin getSkin(){
		return Scene2DToolkit.getToolkit().getSkin();
	}
	public GestureDetector getGesture() {
		return gesture;
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
		eventLogger.begin("RENDER-Screen");
		drawScreen(delta);
		eventLogger.end("RENDER-Screen");

		eventLogger.begin("RENDER-Overlay");
		drawOverlay(delta);
		eventLogger.end("RENDER-Overlay");

		if (drawEvents) {
			eventLogger.begin("RENDER-Event Overlay");
			drawEventOverlay(delta);
			eventLogger.end("RENDER-Event Overlay");

		}

	}

	public void drawEventOverlay(float delta) {
		if (eventBatch == null) {
			eventBatch = new SpriteBatch();
		}
		if(true){
			throw new RuntimeException("NOt working");
		}
		eventBatch.setProjectionMatrix(overlayCamera.combined);
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

	public abstract void drawScreen(float delta);

	public abstract void drawOverlay(float delta);

	@Override
	public void resize(int width, int height) {
		overlayViewport.update(width, height);
		
		screenCamera.viewportWidth = width;
		screenCamera.viewportHeight = height;
	}

	@Override
	public void show() {
		screenCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		overlayCamera = new OrthographicCamera();
		overlayViewport = new ExtendViewport(1024, 800, 1920, 1800, overlayCamera);
		control = new OrthoCamController(screenCamera);
		addInputMultiplexer(parentInputMultiplexer);
	}

	@Override
	public void hide() {
		removeInputMultiplexer(parentInputMultiplexer);
		if (eventBatch != null) {
			eventBatch.dispose();
			eventBatch = null;
		}
		control = null;
		screenCamera= null;
		overlayCamera = null;
		overlayViewport = null;
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
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
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

	public abstract void setupAssetManager(AssetManager assetManager);

	public abstract void clearAssetManager(AssetManager assetManager);

}
