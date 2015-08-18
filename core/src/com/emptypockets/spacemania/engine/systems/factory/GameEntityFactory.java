package com.emptypockets.spacemania.engine.systems.factory;

import com.badlogic.gdx.math.MathUtils;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.collission.CollissionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction.DestructionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.AngularMovementComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.ConstrainedRegionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.network.NetworkDataComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.partition.PartitionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.render.RenderComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.transform.AngularTransformComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.transform.LinearTransformComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.weapon.WeaponComponent;
import com.emptypockets.spacemania.gui.AssetStore;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEntityFactory {
	GameEngine engine;
	AssetStore assetStore;

	public GameEntityFactory(GameEngine engine, AssetStore assetStore) {
		this.engine = engine;
		this.assetStore = assetStore;
	}

	public GameEntity createEntity(GameEntityType type, int id) {
		GameEntity ent = null;
		switch (type) {
		case BULLET:
			ent = createBulletEntity(id);
			break;
		case SHIP:
			ent = createShipEntity(id);
			break;
		default:
			throw new RuntimeException("NOt yet implemented");
		}
		return ent;
	}

	protected GameEntity createBulletEntity(int entityId) {
		float radius = 10;

		GameEntity entity = PoolsManager.obtain(GameEntity.class);
		entity.type = GameEntityType.BULLET;
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

		NetworkDataComponent network = PoolsManager.obtain(NetworkDataComponent.class);
		network.setupData();
		entity.addComponent(network);

		RenderComponent render = PoolsManager.obtain(RenderComponent.class);
		render.setupData();
		render.data.setData(assetStore.getRegion("bullet"), 2 * radius, 2 * radius, true);
		entity.addComponent(render);

		CollissionComponent collission = PoolsManager.obtain(CollissionComponent.class);
		collission.setupData();
		collission.data.collissionRadius = radius;
		entity.addComponent(collission);

		DestructionComponent desctruction = PoolsManager.obtain(DestructionComponent.class);
		desctruction.setupData();
		desctruction.data.destroyTime = System.currentTimeMillis()+10000;
		desctruction.data.remove = false;
		entity.addComponent(desctruction);

		return entity;
	}

	protected GameEntity createShipEntity(int entityId) {
		float radius = 20;

		GameEntity entity = PoolsManager.obtain(GameEntity.class);
		entity.type = GameEntityType.SHIP;
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

		NetworkDataComponent network = PoolsManager.obtain(NetworkDataComponent.class);
		network.setupData();
		entity.addComponent(network);

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

		WeaponComponent weapon = PoolsManager.obtain(WeaponComponent.class);
		weapon.setupData();
		weapon.data.shootTime = MathUtils.random(100, 1000);
		weapon.data.shooting = false;
		weapon.data.bulletVel = 800;
		weapon.data.bulletLife = 5000;
		entity.addComponent(weapon);
		return entity;
	}
}
