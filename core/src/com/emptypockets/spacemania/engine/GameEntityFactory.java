package com.emptypockets.spacemania.engine;

import com.badlogic.gdx.math.MathUtils;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.AngularMovementComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.ConstrainedRegionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.partition.PartitionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.render.RenderComponent;

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
		
		GameEntity entity = new GameEntity(engine, entityId);

		LinearMovementComponent linearMovement = new LinearMovementComponent(entity);
		entity.addComponent(linearMovement);

		AngularMovementComponent angularMovement = new AngularMovementComponent(entity);
		entity.addComponent(angularMovement);

		PartitionComponent partition = new PartitionComponent(entity);
		partition.data.radius = radius;
		
		entity.addComponent(partition);

		ConstrainedRegionComponent constraint = new ConstrainedRegionComponent(entity, engine.universeRegion);
		constraint.data.constrainRadius = radius;

		entity.addComponent(constraint);

		RenderComponent render = new RenderComponent(entity, assetStore.getRegion("playership"), 2 * radius, 2 * radius, true);
		entity.addComponent(render);
		return entity;
	}
}
