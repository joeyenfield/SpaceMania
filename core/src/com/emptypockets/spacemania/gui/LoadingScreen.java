package com.emptypockets.spacemania.gui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.emptypockets.spacemania.MainGame;
import com.emptypockets.spacemania.gui.tools.GameScreen;

public class LoadingScreen extends GameScreen {

	GameScreen nextScreen;

	private Stage stage;

	private Image loadingFrame;
	private Image loadingBarHidden;
	private Image screenBg;
	private Image loadingBg;

	private float startX, endX;
	private float percent;

	private Actor loadingBar;
	
	boolean readyToProceded = false;
	boolean proceededOnLood = false;
	

	public LoadingScreen(MainGame mainGame, InputMultiplexer inputProcessor) {
		super(mainGame, inputProcessor);
	}

	public void setProceededOnLood(boolean proceededOnLood) {
		this.proceededOnLood = proceededOnLood;
	}
	
	@Override
	public void show() {
		super.show();
		readyToProceded = false;
		setupAssetManager(mainGame.getAssetManager());
		mainGame.getAssetManager().finishLoading();
		
		
        // Initialize the stage where we will place everything
        stage = new Stage();

        // Get our textureatlas from the manager
        TextureAtlas atlas = mainGame.getAssetManager().get("loading/loading.pack", TextureAtlas.class);

        // Grab the regions from the atlas and create some images
        loadingFrame = new Image(atlas.findRegion("loading-frame"));
        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
        screenBg = new Image(atlas.findRegion("screen-bg"));
        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

        // Add the loading bar animation
//        Animation anim = new Animation(0.05f, atlas.findRegions("loading-bar-anim") );
//        anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
//        loadingBar = new LoadingBar(anim);

        // Or if you only need a static bar, you can do
         loadingBar = new Image(atlas.findRegion("loading-bar2"));

        // Add all the actors to the stage
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);

        // Add everything to be loaded, for instance:
        nextScreen.setupAssetManager(mainGame.getAssetManager());
        // game.manager.load("data/assets1.pack", TextureAtlas.class);
        // game.manager.load("data/assets2.pack", TextureAtlas.class);
        // game.manager.load("data/assets3.pack", TextureAtlas.class)
	}
	
	public void proceedeToNextScreen(){
		clearAssetManager(mainGame.getAssetManager());
		mainGame.setScreen(nextScreen);
	}
	@Override
	public void updateLogic(float delta) {
		super.updateLogic(delta);
		readyToProceded = (mainGame.getAssetManager().update());
		if(proceededOnLood && readyToProceded){
			proceedeToNextScreen();
		}

		 // Interpolate the percentage to make it more smooth
        percent = Interpolation.linear.apply(percent, mainGame.getAssetManager().getProgress(), 0.1f);

        // Update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setWidth(450 - 450 * percent);
        loadingBg.invalidate();

        // Show the loading screen
        stage.act();
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if(readyToProceded){
			proceedeToNextScreen();
		}
		return super.tap(x, y, count, button);
	}

	@Override
	public void drawScreen(float delta) {
		stage.draw();
	}

	@Override
	public void drawOverlay(float delta) {

	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
		// Set our screen to always be XXX x 480 in size
        stage.getViewport().update(width , height, true);

        // Make the background fill the screen
        screenBg.setSize(width, height);

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame, adjusted a few px
        loadingBar.setX(loadingFrame.getX() + 15);
        loadingBar.setY(loadingFrame.getY() + 5);

        // Place the image that will hide the bar on top of the bar, adjusted a few px
        loadingBarHidden.setX(loadingBar.getX() + 35);
        loadingBarHidden.setY(loadingBar.getY() - 3);
        // The start position and how far to move the hidden loading bar
        startX = loadingBarHidden.getX();
        endX = 440;

        // The rest of the hidden bar
        loadingBg.setSize(450, 50);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setY(loadingBarHidden.getY() + 3);
	}

	public void setNextScreen(GameScreen nextScreen) {
		this.nextScreen = nextScreen;
	}

	@Override
	public void clearAssetManager(AssetManager assetManager) {
		// Tell the manager to load assets for the loading screen
        mainGame.getAssetManager().unload("loading/loading.pack");
	}

	@Override
	public void setupAssetManager(AssetManager assetManager) {
		// Tell the manager to load assets for the loading screen
        mainGame.getAssetManager().load("loading/loading.pack", TextureAtlas.class);
	}

}
