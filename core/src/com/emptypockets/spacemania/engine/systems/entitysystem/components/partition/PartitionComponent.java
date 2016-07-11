package com.emptypockets.spacemania.engine.systems.entitysystem.components.partition;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.gui.tools.ShapeRenderUtil;

public class PartitionComponent extends EntityComponent<PartitionState> {

	// Tag to prevent duplicate searchs
	public int currentSearchId;

	public PartitionComponent() {
		super(ComponentType.PARTITION);
	}

	@Override
	public void debug(ShapeRenderer render, ShapeRenderUtil textRender, Rectangle screenView, Vector2 offset) {
		super.debug(render, textRender, screenView, offset);
		render.setColor(Color.GREEN);
		render.circle(entity.linearTransform.state.pos.x + offset.x, entity.linearTransform.state.pos.y + offset.y, state.radius);
	}
	
	public void update(float deltaTime) {
		if (state == null) {
			return;
		}
		entity.engine.spatialPartition.encodeRange(entity, state.key);
		// Region hasn't changed its ok
		if (state.key.equals(state.lastKey)) {
			return;
		}

		entity.engine.spatialPartition.moveEntity(entity, state.lastKey, state.key);
		state.lastKey.set(state.key);
	}

	@Override
	public Class<PartitionState> getStateClass() {
		return PartitionState.class;
	}

}
