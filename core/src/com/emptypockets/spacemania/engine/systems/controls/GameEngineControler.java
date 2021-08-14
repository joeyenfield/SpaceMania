package com.emptypockets.spacemania.engine.systems.controls;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntityDestructionListener;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.gui.tools.ShapeRenderUtil;
import com.emptypockets.spacemania.utils.CameraHelper;

public class GameEngineControler implements InputProcessor, EntityDestructionListener {

	Vector2 currentTouchWorld = new Vector2();
	Vector2 currentTouchScreen = new Vector2();
	Vector2 lastTouchDownWorld = new Vector2();
	Vector2 lastTouchDownScreen = new Vector2();

	Vector2 renderTemp = new Vector2();

	CameraHelper cameraHelper = new CameraHelper();
	OrthographicCamera camera;
	GameEngine gameEngine;

	GameEntity touchDownEntity;
	GameEntity selectedEntity;

	boolean touchedDownOnSelectedEntity = false;
	boolean touchedDownOnAnyEntity = false;

	public GameEngineControler(OrthographicCamera camera) {
		this.camera = camera;
	}

	public void updateCurrentTouch(int x, int y) {
		currentTouchWorld.x = x;
		currentTouchWorld.y = y;
		currentTouchScreen.x = x;
		currentTouchScreen.x = x;
		cameraHelper.screenToWorld(camera, currentTouchWorld);
		if (gameEngine != null) {
			currentTouchWorld.sub(gameEngine.worldRenderOffset);
		}
	}

	public void updateLastTouch() {
		lastTouchDownScreen.set(currentTouchScreen);
		lastTouchDownWorld.set(currentTouchWorld);
	}

	public float getScreenMove() {
		return currentTouchScreen.dst(lastTouchDownScreen);
	}

	public void render(GameEngine engine, Rectangle screen, ShapeRenderer shapeBatch, SpriteBatch spriteBatch, ShapeRenderUtil textHelper, float pixelSize) {
		if (this.selectedEntity != null) {
			renderTemp.set(this.selectedEntity.getPos()).add(engine.worldRenderOffset);
			shapeBatch.begin(ShapeType.Line);

			if (touchedDownOnSelectedEntity) {
				shapeBatch.setColor(Color.GREEN);
			} else {
				shapeBatch.setColor(Color.WHITE);
			}
			shapeBatch.circle(renderTemp.x, renderTemp.y, 100);
			shapeBatch.end();
		}
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

	public void detatch(GameEntity ent, boolean detatch) {
		if (ent != null && detatch) {
			ent.removeListener(this);
		}
		if (ent == this.selectedEntity) {
			this.selectedEntity = null;
		}
	}

	public void attatch(GameEntity ent) {
		detatch(this.selectedEntity, true);
		if (ent != null) {
			this.selectedEntity = ent;
			if (this.selectedEntity != null) {
				this.selectedEntity.addListener(this);
			}
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		boolean captureInput = false;
		if (gameEngine == null) {
			return false;
		}
		touchedDownOnSelectedEntity = false;
		touchedDownOnAnyEntity = false;

		updateCurrentTouch(screenX, screenY);
		updateLastTouch();
		// Check if entity at point - if so capture input
		touchDownEntity = gameEngine.getEntityAtPos(currentTouchWorld);
		if (touchDownEntity != null) {
			touchedDownOnAnyEntity = true;
			if (selectedEntity != null && touchDownEntity == selectedEntity) {
				touchedDownOnSelectedEntity = true;
			}
		}
		captureInput = touchedDownOnSelectedEntity;
		return captureInput;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		boolean captureInput = false;
		updateCurrentTouch(screenX, screenY);

		// Have selected entity - but touch somewhere else
		if (selectedEntity != null && !touchedDownOnSelectedEntity && getScreenMove() < 10) {
			detatch(this.selectedEntity, true);
		} else if (touchedDownOnSelectedEntity) {
			captureInput = true;
		} else if (touchedDownOnAnyEntity) {
			if (selectedEntity == null && getScreenMove() < 10) {
				// No selected entity
				attatch(touchDownEntity);
				captureInput = true;
			}
		} else if (selectedEntity == null) {
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		boolean captureInput = false;
		// if (touchedDownOnAnyEntity) {
		if (touchedDownOnSelectedEntity) {
			updateCurrentTouch(screenX, screenY);
			captureInput = true;
			if (selectedEntity != null) {
				selectedEntity.getPos().set(currentTouchWorld);
			}
		}
		// }
		return captureInput;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void entityDestruction(GameEntity entity) {
		detatch(entity, false);
	}

	public void setGameEngine(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}

}
