package com.emptypockets.spacemania.engine.systems.entitysystem.components.ai;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntityDestructionListener;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.gui.tools.TextRender;
import com.emptypockets.spacemania.utils.RectangeUtils;

public class AiComponent extends EntityComponent<AiState> implements EntityDestructionListener {

	int targetId = -1;
	Rectangle searchRegion = new Rectangle();

	ArrayList<GameEntity> results = new ArrayList<GameEntity>();
	Vector2 desiredVelocity = new Vector2();
	Vector2 force = new Vector2();
	public float searchSize = 0;

	float lastUpdateTime = 0;
	float updateDelta = -1;

	float lastEntitySearchTime = 0;
	float entitySearchDelta = -1f;

	Vector2 debugP1 = new Vector2();
	Vector2 debugP2 = new Vector2();

	public AiComponent() {
		super(ComponentType.AI);
		showDebug = true;
	}

	@Override
	public void debug(ShapeRenderer render, TextRender textRender, Rectangle screenView, Vector2 offset) {
		super.debug(render, textRender, screenView, offset);
		debugP1.set(entity.linearTransform.state.pos).add(offset).add(force);
		debugP2.set(entity.linearTransform.state.pos).add(offset);
		render.setColor(Color.PURPLE);
		render.line(debugP1, debugP2);
		GameEntity target = entity.engine.getEntityById(targetId);
		if (target != null) {
			debugP1.set(entity.linearTransform.state.pos).add(offset);
			debugP2.set(target.linearTransform.state.pos).add(offset);
			render.setColor(Color.PURPLE);
			render.line(debugP1, debugP2);
			render.setColor(Color.RED);
		} else {
			render.setColor(Color.WHITE);
		}
		updateSearchWindow();
		render.rect(searchRegion.x+offset.x, searchRegion.y+offset.y, searchRegion.width, searchRegion.height);
	}

	public synchronized void updateDirection() {
		LinearMovementComponent lmc = entity.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class);
		// Linear movement can be removed when an entity collides
		if (lmc == null) {
			return;
		}

		// No need to update every itteration - save processing
		force.set(0, 0);
		Vector2 vel = lmc.state.vel;
		GameEntity target = entity.engine.getEntityById(targetId);
		if (target != null) {
			desiredVelocity.set(target.linearTransform.state.pos).sub(entity.linearTransform.state.pos).nor().scl(lmc.state.maxVel);
			force.set(desiredVelocity).sub(lmc.state.vel).setLength(lmc.state.maxVel * 2);
		}
		lmc.state.acl.set(force);
	}
	
	public void updateSearchWindow(){
		searchRegion.x = 0;
		searchRegion.y = 0;
		searchRegion.width = searchSize;
		searchRegion.height = searchSize;
		searchRegion.setCenter(entity.linearTransform.state.pos);
	}

	public synchronized void updateEntity() {
		if (entity.engine.getTime() - lastUpdateTime > updateDelta) {
			updateSearchWindow();
			GameEntity target = entity.engine.getEntityById(targetId);
			if (target != null && !searchRegion.contains(target.linearTransform.state.pos)) {
				clearTarget();
			}
			target = entity.engine.getEntityById(targetId);
			if (target == null && entity.engine.getTime() - lastEntitySearchTime > entitySearchDelta) {
				lastEntitySearchTime = entity.engine.getTime();
				entity.engine.spatialPartition.searchByType(searchRegion, GameEntityType.SHIP, results);
				if (results.size() > 0) {
					target = results.get(0);
					setTarget(target);
					results.clear();
				}
			}
		}
	}

	@Override
	public Class<AiState> getStateClass() {
		return AiState.class;
	}

	@Override
	public void reset() {
		super.reset();
		searchSize = 0;
		lastEntitySearchTime = 0;
		lastUpdateTime = 0;
		force.setZero();
		clearTarget();
	}

	/**
	 * Traget stuff
	 */
	@Override
	public synchronized void entityDestruction(GameEntity entity) {
		if (entity.entityId == targetId) {
			targetId = -1;
		}
	}
	
	public boolean hasTarget() {
		GameEntity target = entity.engine.getEntityById(targetId);
		return target != null;
	}

	public void setTarget(GameEntity target) {
		clearTarget();
		targetId = target.entityId;
		target.addListener(this);
	}

	public void removeTarget(GameEntity target) {
		if (target.entityId == targetId) {
			target.removeListener(this);
		}
		targetId = -1;
	}

	public void clearTarget() {
		if (entity != null && entity.engine != null) {
			GameEntity target = entity.engine.getEntityById(targetId);
			if (target != null) {
				removeTarget(target);
			}
		}
		targetId = -1;
	}

}
