package com.emptypockets.spacemania.engine;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.collission.CollissionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.AngularMovementComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.ConstrainedRegionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.partition.PartitionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.render.RenderComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.AngularTransformComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.LinearTransformComponent;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEntityFactory {
	GameEngine engine;
	AssetStore assetStore;

	public GameEntityFactory(GameEngine engine, AssetStore assetStore) {
		this.engine = engine;
		this.assetStore = assetStore;
	}

	public GameEntity createEntity(int entityId) {
		float radius = 20;
		float vel = 100;

		GameEntity entity = PoolsManager.obtain(GameEntity.class);

		entity.setData(engine, entityId);
		entity.linearTransform = PoolsManager.obtain(LinearTransformComponent.class);
		entity.linearTransform.setupData();
		entity.addComponent(entity.linearTransform);

		entity.angularTransform = PoolsManager.obtain(AngularTransformComponent.class);
		entity.angularTransform.setupData();
		entity.addComponent(entity.angularTransform);

		LinearMovementComponent linearMovement = PoolsManager.obtain(LinearMovementComponent.class);
		linearMovement.setupData();
		entity.addComponent(linearMovement);

		AngularMovementComponent angularMovement = PoolsManager.obtain(AngularMovementComponent.class);
		angularMovement.setupData();
		entity.addComponent(angularMovement);

		PartitionComponent partition = PoolsManager.obtain(PartitionComponent.class);
		partition.setupData();
		partition.data.radius = radius;
		entity.addComponent(partition);

		ConstrainedRegionComponent constraint = PoolsManager.obtain(ConstrainedRegionComponent.class);
		constraint.setupData();
		constraint.data.constrainedRegion = engine.universeRegion;
		constraint.data.constrainRadius = radius;
		entity.addComponent(constraint);

		RenderComponent render = PoolsManager.obtain(RenderComponent.class);
		render.setupData();
		render.data.setData(assetStore.getRegion("playership"), 2 * radius, 2 * radius, true);
		entity.addComponent(render);
		
		CollissionComponent collission = PoolsManager.obtain(CollissionComponent.class);
		collission.setupData();
		collission.data.collissionRadius = radius;
		entity.addComponent(collission);
		
		return entity;
	}
}
