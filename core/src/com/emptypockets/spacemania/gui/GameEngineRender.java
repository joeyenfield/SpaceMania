package com.emptypockets.spacemania.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.render.RenderComponent;

public class GameEngineRender{

	ArrayList<GameEntity> entities = new ArrayList<GameEntity>();

	public void render(SpriteBatch batch, GameEngine engine, Rectangle screenView) {

		engine.spatialPartition.searchAnyMask(screenView, ComponentType.RENDER.getMask(), entities);
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			GameEntity entity = entities.get(i);
			((RenderComponent) entity.getComponent(ComponentType.RENDER)).update(Gdx.graphics.getDeltaTime());
			((RenderComponent) entity.getComponent(ComponentType.RENDER)).render(batch);
		}
		entities.clear();
	}
}
