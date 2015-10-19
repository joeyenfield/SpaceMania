package com.emptypockets.spacemania.engine.systems.entitysystem.components.collission;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ai.AiComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction.DestructionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementState;
import com.emptypockets.spacemania.gui.tools.TextRender;
import com.emptypockets.spacemania.network.old.engine.entities.EntityType;
import com.emptypockets.spacemania.utils.PoolsManager;

public class CollissionComponent extends EntityComponent<CollissionState> {

	ArrayList<GameEntity> entities = new ArrayList<GameEntity>();
	Rectangle region = new Rectangle();

	public CollissionComponent() {
		super(ComponentType.COLLISSION);
		showDebug = false;
	}
	
	@Override
	public void debug(ShapeRenderer render, TextRender textRender, Rectangle screenView, Vector2 offset) {
		super.debug(render, textRender, screenView, offset);
		float radius = state.collissionRadius;
		if (entity.hasAnyOfAbility(ComponentType.DESTRUCTION.getMask())) {
			render.setColor(Color.RED);
		} else if (entity.hasComponent(ComponentType.AI) && entity.getComponent(ComponentType.AI, AiComponent.class).hasTarget()) {
			render.setColor(Color.YELLOW);
		} else {
			render.setColor(Color.WHITE);
		}

		render.circle(entity.linearTransform.state.pos.x + offset.x, entity.linearTransform.state.pos.y + offset.y, radius);
	
	}

	
	public void update(float deltaTime) {
		region.x = entity.linearTransform.state.pos.x - state.collissionRadius;
		region.y = entity.linearTransform.state.pos.y - state.collissionRadius;
		region.width = 2 * state.collissionRadius;
		region.height = 2 * state.collissionRadius;

		entity.engine.spatialPartition.searchAnyMask(region, ComponentType.COLLISSION.getMask(), entities);

		int size = entities.size();

		for (int i = 0; i < size; i++) {
			GameEntity ent = entities.get(i);
			if (ent.entityId != entity.entityId) {
				if (!ent.hasComponent(ComponentType.DESTRUCTION)) {
					float rad = state.collissionRadius + ent.getComponent(ComponentType.COLLISSION, CollissionComponent.class).state.collissionRadius;
					if (ent.linearTransform.state.pos.dst2(entity.linearTransform.state.pos) < rad * rad) {
						destroy(ent);
						destroy(entity);
					}
				}
			}
		}
		entities.clear();
	}

	public void destroy(GameEntity ent) {
		if(ent.type == GameEntityType.SHIP){
			return;
		}
		if (!ent.hasAnyOfAbility(ComponentType.DESTRUCTION.getMask())) {
			DestructionComponent dest = PoolsManager.obtain(DestructionComponent.class);
			dest.setupState();
			dest.state.destroyTime = ent.engine.getTime() + 1;
			ent.addComponent(dest);
		}
		
		if (ent.type != GameEntityType.BULLET && ent.hasAnyOfAbility(ComponentType.LINEAR_MOVEMENT.getMask())) {
			ent.removeComponent(ComponentType.LINEAR_MOVEMENT);
		}
	}

	@Override
	public Class<CollissionState> getStateClass() {
		return CollissionState.class;
	}

}
