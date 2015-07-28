package com.emptypockets.spacemania.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.render.RenderComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class GameEngineRender implements SingleProcessor<GameEntity> {

	public SpriteBatch batch;

	public void render(GameEngine engine, Rectangle screenView) {
		engine.entitySystem.process(this, screenView, ComponentType.RENDER.getMask());
	}

	@Override
	public void process(GameEntity entity) {
		((RenderComponent) entity.getComponent(ComponentType.RENDER)).update(Gdx.graphics.getDeltaTime());
		((RenderComponent) entity.getComponent(ComponentType.RENDER)).render(batch);
	}

}
