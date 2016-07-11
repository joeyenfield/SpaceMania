package com.emptypockets.spacemania.engine.systems.entitysystem.components;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.gui.tools.ShapeRenderUtil;
import com.emptypockets.spacemania.utils.PoolsManager;

public abstract class EntityComponent<STATE_TYPE extends ComponentState> implements Poolable {
	protected GameEntity entity;
	public ComponentType componentType;
	public STATE_TYPE state = null;
	public boolean networkSync = false;
	public boolean showDebug = false;

	public EntityComponent(ComponentType componentType) {
		this.componentType = componentType;
	}

	public abstract Class<STATE_TYPE> getStateClass();

	public ComponentType getComponentType() {
		return componentType;
	}

	public STATE_TYPE createState() {
		return PoolsManager.obtain(getStateClass());
	}

	public void setupState() {
		this.state = createState();
	}

	public STATE_TYPE getState() {
		return state;
	}

	public void setEntity(GameEntity entity) {
		this.entity = entity;
	}

	public void reset() {
		entity = null;
		networkSync = false;
		PoolsManager.free(state);
		state = null;
	}

	public void setData(STATE_TYPE data) {
		this.state = data;
	}

	public boolean dataChanged(STATE_TYPE data) {
		return this.state.hasStateChanged(data);
	}

	public void writeData(STATE_TYPE data) {
		this.state.writeComponentState(data);
	}

	public void readData(STATE_TYPE data) {
		this.state.readComponentState(data);
	}

	public boolean showDebug() {
		return showDebug;
	}

	public boolean shouldRenderDebug( Rectangle screenView, Vector2 offset) {
		return screenView.contains(entity.linearTransform.state.pos.x+offset.x,entity.linearTransform.state.pos.y+offset.y); 
	}

	public void debug(ShapeRenderer render, ShapeRenderUtil textRender, Rectangle screenView, Vector2 offset) {

	}
}
