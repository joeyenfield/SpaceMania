package com.emptypockets.spacemania.engine.systems.entitysystem.components.collission;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction.DestructionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementData;
import com.emptypockets.spacemania.network.old.engine.entities.EntityType;
import com.emptypockets.spacemania.utils.PoolsManager;

public class CollissionComponent extends EntityComponent<CollissionData> {

	ArrayList<GameEntity> entities = new ArrayList<GameEntity>();
	Rectangle region = new Rectangle();

	public CollissionComponent() {
		super(ComponentType.COLLISSION);
	}

	public void update(float deltaTime) {
		region.x = entity.linearTransform.data.pos.x - data.collissionRadius;
		region.y = entity.linearTransform.data.pos.y - data.collissionRadius;
		region.width = 2 * data.collissionRadius;
		region.height = 2 * data.collissionRadius;

		entity.engine.spatialPartition.searchAnyMask(region, ComponentType.COLLISSION.getMask(), entities);

		int size = entities.size();

		for (int i = 0; i < size; i++) {
			GameEntity ent = entities.get(i);
			if (ent.entityId != entity.entityId) {
				if (!ent.hasComponent(ComponentType.DESTRUCTION)) {
					float rad = data.collissionRadius + ent.getComponent(ComponentType.COLLISSION, CollissionComponent.class).data.collissionRadius;
					if (ent.linearTransform.data.pos.dst2(entity.linearTransform.data.pos) < rad * rad) {
						destroy(ent);
						destroy(entity);
					}
				}
			}
		}
		entities.clear();
	}

	public void destroy(GameEntity ent) {
		if (!ent.hasAnyOfAbility(ComponentType.DESTRUCTION.getMask())) {
			DestructionComponent dest = PoolsManager.obtain(DestructionComponent.class);
			dest.setupData();
			dest.data.destroyTime = ent.engine.getTime() + 1;
			ent.addComponent(dest);
		}
		
		if (ent.type != GameEntityType.BULLET && ent.hasAnyOfAbility(ComponentType.LINEAR_MOVEMENT.getMask())) {
			ent.removeComponent(ComponentType.LINEAR_MOVEMENT);
		}
	}

	@Override
	public Class<CollissionData> getDataClass() {
		return CollissionData.class;
	}

}
