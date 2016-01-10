package com.emptypockets.spacemania.engine;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.metrics.plotter.DataLogger;
import com.emptypockets.spacemania.engine.processes.common.CollissionProcess;
import com.emptypockets.spacemania.engine.processes.common.DestructionProcess;
import com.emptypockets.spacemania.engine.processes.common.MovementProcess;
import com.emptypockets.spacemania.engine.processes.common.PartitionProcess;
import com.emptypockets.spacemania.engine.processes.common.WeaponProcess;
import com.emptypockets.spacemania.engine.processes.host.AiProcess;
import com.emptypockets.spacemania.engine.systems.entitysystem.EntitySystem;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.engine.systems.factory.GameEntityFactory;
import com.emptypockets.spacemania.engine.systems.spatialpartition.CellsGameEntitySpatitionPartition;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEngine {
	String name = "ENGINE";
	float engineTime;
	float lastDelta;
	boolean running;

	Vector2 tempPos = new Vector2();
	public Vector2 worldRenderOffset = new Vector2();
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
	public AiProcess aiProcess = new AiProcess();

	int entityCreationCount = 1;

	public GameEngine(float sectorSize, int partitionSize) {
		this.universeRegion.set(-sectorSize / 2, -sectorSize / 2, sectorSize, sectorSize);
		entitySystem = new EntitySystem();
		spatialPartition = new CellsGameEntitySpatitionPartition(this, partitionSize, partitionSize);
	}

	public boolean containsWorld(Vector2 pos) {
		tempPos.set(pos);
		tempPos.sub(worldRenderOffset);
		return universeRegion.contains(tempPos);
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
					DataLogger.log(name + "-ent-" + entity.entityId + "-pos-x", entity.linearTransform.state.pos.x);
					DataLogger.log(name + "-ent-" + entity.entityId + "-pos-y", entity.linearTransform.state.pos.y);

					if (entity.hasComponent(ComponentType.LINEAR_MOVEMENT)) {
						DataLogger.log(name + "-ent-" + entity.entityId + "-vel-x", entity.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).state.vel.x);
						DataLogger.log(name + "-ent-" + entity.entityId + "-vel-y", entity.getComponent(ComponentType.LINEAR_MOVEMENT, LinearMovementComponent.class).state.vel.y);
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
