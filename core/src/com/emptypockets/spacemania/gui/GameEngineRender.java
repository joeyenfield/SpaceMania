package com.emptypockets.spacemania.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.engines.GameEngine;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.render.RenderComponent;
import com.emptypockets.spacemania.gui.tools.TextRender;

public class GameEngineRender {
	boolean showCellsSpace = false;

	ArrayList<GameEntity> entities = new ArrayList<GameEntity>();

	public void render(SpriteBatch batch, GameEngine engine, Rectangle screenView) {
		batch.begin();
		engine.spatialPartition.searchAnyMask(screenView, ComponentType.RENDER.getMask(), entities);
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			GameEntity entity = entities.get(i);
			((RenderComponent) entity.getComponent(ComponentType.RENDER)).update(Gdx.graphics.getDeltaTime());
			((RenderComponent) entity.getComponent(ComponentType.RENDER)).render(batch);
		}
		entities.clear();
		batch.end();
	}

	public void renderDebug(ShapeRenderer render, GameEngine engine, TextRender textHelper, Rectangle screenView) {
		if (showCellsSpace) {
			engine.spatialPartition.renderDebug(render, textHelper, screenView);
		}
		
		render.begin(ShapeType.Line);
		render.setColor(Color.RED);
		render.rect(engine.universeRegion.x, engine.universeRegion.y, engine.universeRegion.width, engine.universeRegion.height);

		engine.spatialPartition.searchAnyMask(screenView, ComponentType.RENDER.getMask(), entities);
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			GameEntity entity = entities.get(i);
			((RenderComponent) entity.getComponent(ComponentType.RENDER)).update(Gdx.graphics.getDeltaTime());
			((RenderComponent) entity.getComponent(ComponentType.RENDER)).renderDebug(render);
		}
		entities.clear();
		render.end();
	}
}
