package com.emptypockets.spacemania.engine.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.emptypockets.spacemania.screens.GameEngineScreen;

public class DebugOnScreenPlayerInputProducer extends Table implements PlayerInputProducer {
	int minTouchSize = 60;
	int insetSize = 50;
	int touchPadSize = 200;

	int keyMoveUp = Keys.W;
	int keyMoveDown = Keys.S;
	int keyMoveLeft = Keys.A;
	int keyMoveRight = Keys.D;

	int keyShootUp = Keys.UP;
	int keyShootDown = Keys.DOWN;
	int keyShootLeft = Keys.LEFT;
	int keyShootRight = Keys.RIGHT;

	int keyShoot = Keys.SPACE;

	Touchpad movePad;
	Touchpad shootPad;

	TextButton shootButton;
	TextButton debugButton;
	TextButton centerEntityButton;
	TextButton centerServerButton;

	PlayerInputData playerInput = new PlayerInputData();
	GameEngineScreen gameScreen;

	public DebugOnScreenPlayerInputProducer(Skin skin, GameEngineScreen gameScreen) {
		super(skin);
		this.gameScreen = gameScreen;
		createGUI();
		layoutGui();
	}

	public PlayerInputData getPlayerInput() {
		return playerInput;
	}

	public void createGUI() {
		shootButton = new TextButton("", getSkin());
		debugButton = new TextButton("D", getSkin());
		centerEntityButton = new TextButton("FIX", getSkin());
		centerServerButton = new TextButton("CLI", getSkin());

		movePad = new Touchpad(0, getSkin());
		shootPad = new Touchpad(0, getSkin());
		Pixmap pix = new Pixmap(4, 4, Format.RGBA8888);
		Color c = new Color(Color.LIGHT_GRAY);
		c.a = 0.2f;
		pix.setColor(c);
		pix.fill();
		pix.setColor(Color.LIGHT_GRAY);
		pix.drawRectangle(0, 0, 4, 4);

		NinePatch ninePatch = new NinePatch(new Texture(pix), 1, 1, 1, 1);
		Drawable draw = new NinePatchDrawable(ninePatch);
		movePad.getStyle().background = draw;
		shootPad.getStyle().background = draw;

		debugButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameScreen.debugDisplay = debugButton.isChecked();
			}
		});

		if(gameScreen.centerOnEntity){
			centerEntityButton.setText("FREE");
		}else{
			centerEntityButton.setText("LOCK");
		}
		centerEntityButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameScreen.centerOnEntity = centerEntityButton.isChecked();
				
				if(gameScreen.centerOnEntity){
					centerEntityButton.setText("FREE");
				}else{
					centerEntityButton.setText("LOCK");
				}
			}
		});

		if(gameScreen.centerOnServer){
			centerServerButton.setText("SVR");
		}else{
			centerServerButton.setText("CLI");
		}
		centerServerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameScreen.centerOnServer = centerServerButton.isChecked();
				if(gameScreen.centerOnServer){
					centerServerButton.setText("SVR");
				}else{
					centerServerButton.setText("CLI");
				}
			}
		});
	}

	public void layoutGui() {
		Table controlsHolder = new Table();
		// top
		controlsHolder.row();
		controlsHolder.add();
		controlsHolder.add().fillX().expandX();
		controlsHolder.add();

		// middle
		controlsHolder.row();
		controlsHolder.add().fillY().expandY();
		controlsHolder.add().fill().expand();
		controlsHolder.add().fillY().expandY();

		// bottom
		controlsHolder.row();
		controlsHolder.add(movePad).width(touchPadSize).size(touchPadSize);
		controlsHolder.add().fillX().expandX();
		controlsHolder.add(shootPad).width(touchPadSize).size(touchPadSize);

		Table inset = new Table();
		inset.row();
		inset.add(centerEntityButton).size(minTouchSize, minTouchSize);
		inset.add().height(insetSize).expandX().fillX();
		inset.add(centerServerButton).size(minTouchSize, minTouchSize);

		inset.row();
		inset.add().width(insetSize).expandY().fillY();
		inset.add(controlsHolder).fill().expand();
		inset.add().width(insetSize).expandY().fillY();

		inset.row();
		inset.add(debugButton).size(minTouchSize, minTouchSize);
		inset.add(shootButton).height(insetSize).expandX().fillX();
		inset.add();

		inset.setFillParent(true);
		clear();
		setFillParent(true);
		add(inset).fill().expand();
	}

	public void update() {
		getPlayerInput().move.setZero();
		getPlayerInput().move.set(movePad.getKnobPercentX(), movePad.getKnobPercentY());

		if (Gdx.input.isKeyPressed(keyMoveRight)) {
			getPlayerInput().move.x += +1;
		}
		if (Gdx.input.isKeyPressed(keyMoveLeft)) {
			getPlayerInput().move.x += -1;
		}

		if (Gdx.input.isKeyPressed(keyMoveUp)) {
			getPlayerInput().move.y += +1;
		}
		if (Gdx.input.isKeyPressed(keyMoveDown)) {
			getPlayerInput().move.y += -1;
		}

		getPlayerInput().shootDir.setZero();
		getPlayerInput().shootDir.set(shootPad.getKnobPercentX(), shootPad.getKnobPercentY());

		if (Gdx.input.isKeyPressed(keyShootRight)) {
			getPlayerInput().shootDir.x += +1;
		}
		if (Gdx.input.isKeyPressed(keyShootLeft)) {
			getPlayerInput().shootDir.x += -1;
		}

		if (Gdx.input.isKeyPressed(keyShootUp)) {
			getPlayerInput().shootDir.y += +1;
		}
		if (Gdx.input.isKeyPressed(keyShootDown)) {
			getPlayerInput().shootDir.y += -1;
		}

		getPlayerInput().move.limit2(1);
		getPlayerInput().shootDir.limit2(1);

		if (Gdx.input.isKeyPressed(keyShoot) || getPlayerInput().shootDir.len2() > 0.1f || shootButton.isPressed()) {
			getPlayerInput().shoot = true;
		} else {
			getPlayerInput().shoot = false;
		}
	}

}
