package com.emptypockets.spacemania.engine.systems.factory;

import com.badlogic.gdx.math.MathUtils;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ai.AiComponent;
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
import com.emptypockets.spacemania.screens.GameEngineScreen;
import com.emptypockets.spacemania.utils.PoolsManager;

public abstract class GameEntityFactory {
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
		case ENEMY:
			ent = createEnemyEntity(id);
			break;
		case PLAYER_BASE:
			ent = createPlayerBase(id);
			break;
		default:
			throw new RuntimeException("Not yet implemented");
		}
		return ent;
	}

	protected GameEntity createPlayerBase(int entityId) {
		float radius = 128;

		GameEntity entity = PoolsManager.obtain(GameEntity.class);
		entity.type = GameEntityType.PLAYER_BASE;
		entity.setData(engine, entityId);

		// Add Components
		entity.addComponent(ComponentType.LINEAR_TRANSFORM);
		entity.addComponent(ComponentType.ANGULAR_TRANSFORM);
		entity.addComponent(ComponentType.ANGULAR_MOVEMENT);
		entity.addComponent(ComponentType.PARTITION);
		entity.addComponent(ComponentType.RENDER);
		entity.addComponent(ComponentType.NETWORK_DATA);

		// Setup some data
		entity.getComponent(ComponentType.PARTITION, PartitionComponent.class).state.radius = radius;
		entity.getComponent(ComponentType.RENDER, RenderComponent.class).state.setData(assetStore.getRegion("player-base"), 2 * radius, 2 * radius, true, 0);
		entity.getComponent(ComponentType.ANGULAR_MOVEMENT, AngularMovementComponent.class).state.angVel = 3;
		return entity;
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
		entity.getComponent(ComponentType.PARTITION, PartitionComponent.class).state.radius = radius;
		entity.getComponent(ComponentType.RENDER, RenderComponent.class).state.setData(assetStore.getRegion("bullet"), 2 * radius, 2 * radius, true,2);
		entity.getComponent(ComponentType.COLLISSION, CollissionComponent.class).state.collissionRadius = radius;
		entity.getComponent(ComponentType.DESTRUCTION, DestructionComponent.class).state.destroyTime = engine.getTime() + 10;
		entity.getComponent(ComponentType.DESTRUCTION, DestructionComponent.class).state.remove = false;

		LinearMovementComponent comp = (LinearMovementComponent) entity.getComponent(ComponentType.LINEAR_MOVEMENT);
		comp.state.maxVel = GameEngineScreen.bulletVel;

		return entity;
	}

	protected GameEntity createEnemyEntity(int entityId) {
		float radius = 20;

		GameEntity entity = PoolsManager.obtain(GameEntity.class);
		entity.type = GameEntityType.ENEMY;
		entity.setData(engine, entityId);

		entity.addComponent(ComponentType.LINEAR_TRANSFORM);
		entity.addComponent(ComponentType.LINEAR_MOVEMENT);
		entity.addComponent(ComponentType.ANGULAR_TRANSFORM);
		entity.addComponent(ComponentType.ANGULAR_MOVEMENT);
		entity.addComponent(ComponentType.PARTITION);
		entity.addComponent(ComponentType.COLLISSION);
		entity.addComponent(ComponentType.RENDER);
		entity.addComponent(ComponentType.NETWORK_DATA);
		entity.addComponent(ComponentType.CONSTRAINED_MOVEMENT);
		entity.addComponent(ComponentType.WEAPON);
		entity.addComponent(ComponentType.AI);

		entity.getComponent(ComponentType.PARTITION, PartitionComponent.class).state.radius = radius;
		entity.getComponent(ComponentType.RENDER, RenderComponent.class).state.setData(assetStore.getRegion("enemy-follow"), 2 * radius, 2 * radius, true,2);
		entity.getComponent(ComponentType.COLLISSION, CollissionComponent.class).state.collissionRadius = radius;
		entity.getComponent(ComponentType.CONSTRAINED_MOVEMENT, ConstrainedRegionComponent.class).state.constrainedRegion = engine.universeRegion;
		entity.getComponent(ComponentType.CONSTRAINED_MOVEMENT, ConstrainedRegionComponent.class).state.constrainRadius = radius;

		WeaponComponent weapon = entity.getComponent(ComponentType.WEAPON, WeaponComponent.class);
		weapon.state.shootTime = MathUtils.random(GameEngineScreen.bulletShootTimeMin, GameEngineScreen.bulletShootTimeMax);
		weapon.state.shooting = false;
		weapon.state.bulletVel = GameEngineScreen.bulletVel;
		weapon.state.bulletLife = 5;

		LinearMovementComponent comp = (LinearMovementComponent) entity.getComponent(ComponentType.LINEAR_MOVEMENT);
		// float progress = (0.1f + 0.8f * (i / (ents - 1f)));
		// entity.linearTransform.data.pos.x = width * progress;
		// entity.linearTransform.data.pos.y = height * progress;
		// comp.data.vel.x = 10;?
		comp.state.vel.x = MathUtils.random(GameEngineScreen.minVelEnemy, GameEngineScreen.maxVelEnemy) * MathUtils.randomSign();
		comp.state.vel.y = MathUtils.random(GameEngineScreen.minVelEnemy, GameEngineScreen.maxVelEnemy) * MathUtils.randomSign();
		comp.state.maxVel = GameEngineScreen.maxVelEnemy;
		entity.linearTransform.state.pos.x = MathUtils.random(engine.universeRegion.x, engine.universeRegion.x + engine.universeRegion.width);
		entity.linearTransform.state.pos.y = MathUtils.random(engine.universeRegion.y, engine.universeRegion.y + engine.universeRegion.height);

		entity.getComponent(ComponentType.AI, AiComponent.class).searchSize = GameEngineScreen.enemySearchWindow;

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
		entity.getComponent(ComponentType.PARTITION, PartitionComponent.class).state.radius = radius;
		entity.getComponent(ComponentType.RENDER, RenderComponent.class).state.setData(assetStore.getRegion("playership"), 2 * radius, 2 * radius, true,1);
		entity.getComponent(ComponentType.COLLISSION, CollissionComponent.class).state.collissionRadius = radius;
		entity.getComponent(ComponentType.CONSTRAINED_MOVEMENT, ConstrainedRegionComponent.class).state.constrainedRegion = engine.universeRegion;
		entity.getComponent(ComponentType.CONSTRAINED_MOVEMENT, ConstrainedRegionComponent.class).state.constrainRadius = radius;

		WeaponComponent weapon = entity.getComponent(ComponentType.WEAPON, WeaponComponent.class);
		weapon.state.shootTime = MathUtils.random(GameEngineScreen.bulletShootTimeMin, GameEngineScreen.bulletShootTimeMax);
		weapon.state.shooting = false;
		weapon.state.bulletVel = GameEngineScreen.bulletVel;
		weapon.state.bulletLife = 5;

		LinearMovementComponent comp = (LinearMovementComponent) entity.getComponent(ComponentType.LINEAR_MOVEMENT);
		// float progress = (0.1f + 0.8f * (i / (ents - 1f)));
		// entity.linearTransform.data.pos.x = width * progress;
		// entity.linearTransform.data.pos.y = height * progress;
		// comp.data.vel.x = 10;?
		comp.state.vel.x = 0;
		comp.state.vel.y = 0;
		comp.state.maxVel = GameEngineScreen.velShip;
		entity.linearTransform.state.pos.x = MathUtils.random(engine.universeRegion.x, engine.universeRegion.x + engine.universeRegion.width);
		entity.linearTransform.state.pos.y = MathUtils.random(engine.universeRegion.y, engine.universeRegion.y + engine.universeRegion.height);

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
		case AI:
			component = PoolsManager.obtain(AiComponent.class);
			break;
		default:
			System.out.println(type);
			throw new RuntimeException("Unknown Component");
		}
		component.setupState();
		return component;
	}
}
