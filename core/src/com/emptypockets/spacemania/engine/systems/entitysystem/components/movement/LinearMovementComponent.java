package com.emptypockets.spacemania.engine.systems.entitysystem.components.movement;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.gui.tools.ShapeRenderUtil;
import com.emptypockets.spacemania.utils.RectangeUtils;

public class LinearMovementComponent extends EntityComponent<LinearMovementState> {
	static float MIN_DELTA = 0.01f;
	Vector2 tempP1 = new Vector2();
	Vector2 tempP2 = new Vector2();

	public LinearMovementComponent() {
		super(ComponentType.LINEAR_MOVEMENT);
		showDebug = true;
	}

	@Override
	public void setupState() {
		super.setupState();
		networkSync = true;
	}

	@Override
	public void debug(ShapeRenderer render, ShapeRenderUtil textRender, Rectangle screenView, Vector2 offset) {
		super.debug(render, textRender, screenView, offset);
		tempP1.set(entity.linearTransform.state.pos).add(offset);
		tempP2.set(entity.linearTransform.state.pos).add(offset).add(state.vel);
		render.setColor(Color.YELLOW);
		render.line(tempP1, tempP2);

		tempP1.set(entity.linearTransform.state.pos).add(offset);
		tempP2.set(entity.linearTransform.state.pos).add(offset).add(state.acl);
		render.setColor(Color.ORANGE);
		render.line(tempP1, tempP2);
	}

	public void update(float deltaTime) {
		if (state == null) {
			return;
		}
		if (state.acl.len2() > MIN_DELTA) {
			state.vel.x += state.acl.x * deltaTime;
			state.vel.y += state.acl.y * deltaTime;
		}
		state.acl.x = 0;
		state.acl.y = 0;

		state.vel.limit2(state.maxVel * state.maxVel);
		if (state.vel.len2() > MIN_DELTA) {
			entity.linearTransform.state.pos.x += state.vel.x * deltaTime;
			entity.linearTransform.state.pos.y += state.vel.y * deltaTime;
		} else {
			state.vel.x = 0;
			state.vel.y = 0;
		}

	}

	@Override
	public Class<LinearMovementState> getStateClass() {
		return LinearMovementState.class;
	}
}
