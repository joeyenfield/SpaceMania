package com.emptypockets.spacemania.engine;

import com.badlogic.gdx.math.MathUtils;
import com.emptypockets.spacemania.engine.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.AngularMovementComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.ConstrainedRegionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.partition.PartitionComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.render.RenderComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.AngularTransformComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.LinearTransformComponent;

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
		entity.linearTransform = new LinearTransformComponent();
		entity.angularTransform = new AngularTransformComponent();
		
		entity.addComponent(entity.linearTransform);
		entity.addComponent(entity.angularTransform);
		
		LinearMovementComponent linearMovement = new LinearMovementComponent();
		entity.addComponent(linearMovement);

		AngularMovementComponent angularMovement = new AngularMovementComponent();
		entity.addComponent(angularMovement);

		PartitionComponent partition = new PartitionComponent();
		entity.addComponent(partition);
		partition.data.radius = radius;


		ConstrainedRegionComponent constraint = new ConstrainedRegionComponent();
		entity.addComponent(constraint);
		constraint.data.constrainedRegion = engine.universeRegion;
		constraint.data.constrainRadius = radius;

		RenderComponent render = new RenderComponent();
		entity.addComponent(render);
		render.data.setData(assetStore.getRegion("playership"), 2 * radius, 2 * radius, true);
		return entity;
	}
}
