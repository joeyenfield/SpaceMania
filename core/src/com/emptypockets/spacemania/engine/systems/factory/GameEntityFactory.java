package com.emptypockets.spacemania.engine.systems.factory;

import com.badlogic.gdx.math.MathUtils;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.collission.CollissionComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.controls.ControlComponent;
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
			throw new RuntimeException("Not yet implemented");
		}
		return ent;
	}

	protected GameEntity createBulletEntity(int entityId) {
		float radius = 10;

		GameEntity entity = PoolsManager.obtain(GameEntity.class);
		entity.type = GameEntityType.BULLET;
		entity.setData(engine, entityId);

		// Add Components
		entity.addComponent(ComponentType.LINEAR_TRANSFORM);
		entity.addComponent(ComponentType.ANGULAR_TRANSFORM);
		entity.addComponent(ComponentType.LINEAR_MOVEMENT);
		entity.addComponent(ComponentType.ANGULAR_MOVEMENT);
		entity.addComponent(ComponentType.PARTITION);
		entity.addComponent(ComponentType.RENDER);
		entity.addComponent(ComponentType.NETWORK_DATA);
		entity.addComponent(ComponentType.COLLISSION);
		entity.addComponent(ComponentType.DESTRUCTION);

		// Setup some data
		entity.getComponent(ComponentType.PARTITION, PartitionComponent.class).data.radius = radius;
		entity.getComponent(ComponentType.RENDER, RenderComponent.class).data.setData(assetStore.getRegion("bullet"), 2 * radius, 2 * radius, true);
		entity.getComponent(ComponentType.COLLISSION, CollissionComponent.class).data.collissionRadius = radius;
		entity.getComponent(ComponentType.DESTRUCTION, DestructionComponent.class).data.destroyTime = engine.getTime() + 10;
		entity.getComponent(ComponentType.DESTRUCTION, DestructionComponent.class).data.remove = false;
		return entity;
	}

	protected GameEntity createShipEntity(int entityId) {
		float radius = 20;

		GameEntity entity = PoolsManager.obtain(GameEntity.class);
		entity.type = GameEntityType.SHIP;
		entity.setData(engine, entityId);

		LinearTransformComponent linearTransform = (LinearTransformComponent) entity.addComponent(ComponentType.LINEAR_TRANSFORM);
		LinearMovementComponent linearMovement = (LinearMovementComponent) entity.addComponent(ComponentType.LINEAR_MOVEMENT);
		
		AngularTransformComponent angularTransform = (AngularTransformComponent) entity.addComponent(ComponentType.ANGULAR_TRANSFORM);
		AngularMovementComponent angularMovement = (AngularMovementComponent) entity.addComponent(ComponentType.ANGULAR_MOVEMENT);
		
		PartitionComponent partition = (PartitionComponent) entity.addComponent(ComponentType.PARTITION);
		CollissionComponent collission = (CollissionComponent) entity.addComponent(ComponentType.COLLISSION);
		
		RenderComponent render = (RenderComponent) entity.addComponent(ComponentType.RENDER);
		
		NetworkDataComponent network = (NetworkDataComponent) entity.addComponent(ComponentType.NETWORK_DATA);
		
		entity.addComponent(ComponentType.CONSTRAINED_MOVEMENT);
		entity.addComponent(ComponentType.WEAPON);

		entity.getComponent(ComponentType.PARTITION, PartitionComponent.class).data.radius = radius;
		entity.getComponent(ComponentType.RENDER, RenderComponent.class).data.setData(assetStore.getRegion("playership"), 2 * radius, 2 * radius, true);
		entity.getComponent(ComponentType.COLLISSION, CollissionComponent.class).data.collissionRadius = radius;
		entity.getComponent(ComponentType.CONSTRAINED_MOVEMENT, ConstrainedRegionComponent.class).data.constrainedRegion = engine.universeRegion;
		entity.getComponent(ComponentType.CONSTRAINED_MOVEMENT, ConstrainedRegionComponent.class).data.constrainRadius = radius;

		WeaponComponent weapon = entity.getComponent(ComponentType.WEAPON, WeaponComponent.class);
		weapon.data.shootTime = MathUtils.random(100, 1000);
		weapon.data.shooting = false;
		weapon.data.bulletVel = 550;
		weapon.data.bulletLife = 5;
		return entity;
	}

	public EntityComponent createComponent(ComponentType type) {
		EntityComponent component = null;
		switch (type) {
		case ANGULAR_MOVEMENT:
			component = PoolsManager.obtain(AngularMovementComponent.class);
			break;
		case ANGULAR_TRANSFORM:
			component = PoolsManager.obtain(AngularTransformComponent.class);
			break;
		case COLLISSION:
			component = PoolsManager.obtain(CollissionComponent.class);
			break;
		case CONSTRAINED_MOVEMENT:
			component = PoolsManager.obtain(ConstrainedRegionComponent.class);
			break;
		case CONTROL:
			component = PoolsManager.obtain(ControlComponent.class);
			break;
		case DESTRUCTION:
			component = PoolsManager.obtain(DestructionComponent.class);
			break;
		case LINEAR_MOVEMENT:
			component = PoolsManager.obtain(LinearMovementComponent.class);
			break;
		case LINEAR_TRANSFORM:
			component = PoolsManager.obtain(LinearTransformComponent.class);
			break;
		case NETWORK_DATA:
			component = PoolsManager.obtain(NetworkDataComponent.class);
			break;
		case PARTITION:
			component = PoolsManager.obtain(PartitionComponent.class);
			break;
		case RENDER:
			component = PoolsManager.obtain(RenderComponent.class);
			break;
		case WEAPON:
			component = PoolsManager.obtain(WeaponComponent.class);
			break;
		default:
			throw new RuntimeException("Unknown Component");
		}
		component.setupData();
		return component;
	}
}
