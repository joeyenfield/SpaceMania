package com.emptypockets.spacemania.engine;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.engine.processes.common.CollissionProcess;
import com.emptypockets.spacemania.engine.processes.common.DestructionProcess;
import com.emptypockets.spacemania.engine.processes.common.MovementProcess;
import com.emptypockets.spacemania.engine.processes.common.PartitionProcess;
import com.emptypockets.spacemania.engine.processes.common.WeaponProcess;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.systems.factory.GameEntityFactory;
import com.emptypockets.spacemania.engine.systems.spatialpartition.CellsGameEntitySpatitionPartition;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.metrics.plotter.DataLogger;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEngine {
	String name = "ENGINE";
	public Vector2 posOffset = new Vector2();
	float engineTime;
	float lastDelta;
	boolean running;

	public Rectangle universeRegion = new Rectangle();

	public EntitySystem entitySystem;
	public CellsGameEntitySpatitionPartition spatialPartition;
	public GameEntityFactory entityFactory;

	ArrayList<EngineProcess> processes;
	public MovementProcess movementProcess = new MovementProcess();
	public PartitionProcess partitionProcess = new PartitionProcess();
	public CollissionProcess collissionProcess = new CollissionProcess();
	public DestructionProcess destructionProcess = new DestructionProcess();
	public WeaponProcess weaponProcess = new WeaponProcess();

	int entityCreationCount = 1;

	public GameEngine() {
		this.universeRegion.set(-Constants.DEFAULT_ROOM_SIZE, -Constants.DEFAULT_ROOM_SIZE, 2 * Constants.DEFAULT_ROOM_SIZE, 2 * Constants.DEFAULT_ROOM_SIZE);
		entitySystem = new EntitySystem();
		spatialPartition = new CellsGameEntitySpatitionPartition(this, Constants.ENTITY_SYSTEM_PARTITION_X, Constants.ENTITY_SYSTEM_PARTITION_Y);
	}

	public void setUniverseSize(float x, float y, float width, float height) {
		universeRegion.x = x;
		universeRegion.y = y;
		universeRegion.width = width;
		universeRegion.height = height;
	}

	public void update(float deltaTime) {
		if (DataLogger.isEnabled()) {
			DataLogger.log(name + "-UPDATE", 1);
			entitySystem.process(new SingleProcessor<GameEntity>() {
				@Override
				public void process(GameEntity entity) {
					DataLogger.log(name+"-ent-"+entity.entityId+"-pos-x", entity.linearTransform.data.pos.x);
					DataLogger.log(name+"-ent-"+entity.entityId+"-pos-y", entity.linearTransform.data.pos.y);
					
					if(entity.hasComponent(ComponentType.LINEAR_MOVEMENT)){
						DataLogger.log(name+"-ent-"+entity.entityId+"-vel-x", entity.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).data.vel.x);
						DataLogger.log(name+"-ent-"+entity.entityId+"-vel-y", entity.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).data.vel.y);
					}
					
				}
			});
		}
		lastDelta = deltaTime;
		engineTime += deltaTime;

		int size = processes.size();
		for (int i = 0; i < size; i++) {
			processes.get(i).preProcess(this);
		}

		for (int i = 0; i < size; i++) {
			processes.get(i).process(this);
		}

		for (int i = 0; i < size; i++) {
			processes.get(i).postProcess(this);
		}
	}

	public GameEntity getEntityAtPos(Vector2 tempPos) {
		return spatialPartition.getFirstEntityAtPos(tempPos);
	}

	public float getTime() {
		return engineTime;
	}

	public GameEntity createEntity(GameEntityType type, int id) {
		// println("Create : [" + type.name() + "] - " + id);
		GameEntity entity = entityFactory.createEntity(type, id);
		addEntity(entity);
		return entity;
	}

	public GameEntity createEntity(GameEntityType type) {
		// println("Create : [" + type.name() + "]");
		entityCreationCount++;
		return createEntity(type, entityCreationCount);
	}

	public void addEntity(GameEntity entity) {
		// println("Add : [" + entity.type.name() + "] - " + entity.entityId);
		entitySystem.add(entity);
		entity.addListener(spatialPartition);
	}

	public void removeEntity(GameEntity ent) {
		// println("Remove ENT : " + ent.entityId);
		ent.notifyDestroyed();
		PoolsManager.free(ent);
	}

	public GameEntity getEntityById(int entityId) {
		GameEntity entity = entitySystem.getEntityById(entityId);
		return entity;
	}

	public void removeEntity(int id) {
		// println("Remove : " + id);
		GameEntity entity = getEntityById(id);
		if (entity == null) {
			// throw new RuntimeException("Null Entity");
		} else {
			removeEntity(entity);
		}
	}

	public float getDeltaTime() {
		return lastDelta;
	}

	protected void setProcesses(ArrayList<EngineProcess> processes) {
		this.processes = processes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void println(String message) {
		print(message);
		System.out.println();
	}

	public void print(String message) {
		System.out.printf("%s : %s", name, message);
	}

	public String getName() {
		return name;
	}

}
