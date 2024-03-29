package com.emptypockets.spacemania.gui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.render.RenderComponent;
import com.emptypockets.spacemania.gui.tools.ShapeRenderUtil;

public class GameEngineEntitiesRender {
	public boolean showDebug = false;
	boolean showCellsSpace = false;
	ArrayList<GameEntity> entities = new ArrayList<GameEntity>();
	Rectangle screenView = new Rectangle();

	public void renderBounds(GameEngine engine, Rectangle screen, ShapeRenderer shapeBatch, Color color){
		Vector2 offset = engine.worldRenderOffset;
		
		shapeBatch.begin(ShapeType.Line);
		shapeBatch.setColor(color);
		shapeBatch.rect(engine.universeRegion.x + offset.x, engine.universeRegion.y + offset.y, engine.universeRegion.width, engine.universeRegion.height);
		shapeBatch.end();

	}
	public void render(GameEngine engine, Rectangle screen, ShapeRenderer shapeBatch, SpriteBatch spriteBatch, ShapeRenderUtil textHelper, float pixelSize) {
		screenView.set(screen);

		Vector2 offset = engine.worldRenderOffset;
		screenView.x -= offset.x;
		screenView.y -= offset.y;
		engine.spatialPartition.searchAnyMask(screenView, ComponentType.RENDER.getMask(), entities);
		screenView.x += offset.x;
		screenView.y += offset.y;

		int size = entities.size();
		for (int i = 0; i < size; i++) {
			GameEntity entity = entities.get(i);
			((RenderComponent) entity.getComponent(ComponentType.RENDER)).update(offset);
		}

		spriteBatch.begin();
		for (int pass = 0; pass < 3; pass++) {
			for (int i = 0; i < size; i++) {
				GameEntity entity = entities.get(i);
				RenderComponent comp = ((RenderComponent) entity.getComponent(ComponentType.RENDER));
				if (comp.needsRender(pass)){
					comp.render(spriteBatch, pass);
				}
			}
		}
		spriteBatch.end();

		if (showDebug) {
			if (showCellsSpace) {
				engine.spatialPartition.renderDebug(shapeBatch, textHelper, screenView, pixelSize, offset);
			}

			shapeBatch.begin(ShapeType.Line);
			for (int i = 0; i < size; i++) {
				GameEntity entity = entities.get(i);
				((RenderComponent) entity.getComponent(ComponentType.RENDER)).renderDebug(shapeBatch, textHelper, screenView, offset);
			}
			shapeBatch.end();
		}
		entities.clear();

	}
}
