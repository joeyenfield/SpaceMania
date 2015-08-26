package com.emptypockets.spacemania.engine.systems.entitysystem;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponentStore;
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
import com.emptypockets.spacemania.utils.BitUtilities;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEntity implements Poolable {

	public int entityId;
	public int componentsMask;
	public EntityComponentStore componentStore;

	public LinearTransformComponent linearTransform;
	public AngularTransformComponent angularTransform;

	public GameEngine engine;
	public GameEntityType type;

	public ArrayList<EntityDestructionListener> destructionListeners = new ArrayList<EntityDestructionListener>();

	public GameEntity() {
		componentStore = new EntityComponentStore();
	}

	public void setData(GameEngine engine, int entityId) {
		this.entityId = entityId;
		this.engine = engine;
	}

	public void addComponent(EntityComponent<?> component) {
		componentsMask = component.getComponentType().addAbility(componentsMask);
		componentStore.add(component);
		component.setEntity(this);
	}

	public void removeComponent(EntityComponent<?> component) {
		componentsMask = component.getComponentType().removeAbility(componentsMask);
		componentStore.remove(component.getComponentType());
	}

	public EntityComponent<?> getComponent(ComponentType type) {
		return componentStore.get(type);
	}

	public boolean hasAnyOfAbility(int abilities) {
		return (componentsMask & abilities) != 0;
	}

	public boolean hasAllOfAbility(int abilities) {
		return (componentsMask & abilities) == abilities;
	}

	public String getMaskString() {
		return BitUtilities.toString(componentsMask);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof GameEntity) {
			GameEntity ent = (GameEntity) obj;
			return ent.entityId == entityId;
		}
		return false;
	}

	@Override
	public void reset() {
		engine = null;
		entityId = -1;
		componentStore.clear();
		linearTransform = null;
		angularTransform = null;
		componentsMask = 0;
		synchronized (destructionListeners) {
			destructionListeners.clear();
		}
	}

	public void addListener(EntityDestructionListener listener) {
		synchronized (destructionListeners) {
			destructionListeners.add(listener);
		}
	}

	public void removeListener(EntityDestructionListener listener) {
		synchronized (destructionListeners) {
			destructionListeners.remove(listener);
		}
	}

	public void notifyDestroyed() {
		synchronized (destructionListeners) {
			int size = destructionListeners.size();
			for (int i = 0; i < size; i++) {
				destructionListeners.get(i).entityDestruction(this);
			}
			destructionListeners.clear();
		}
	}

	public <COMP extends EntityComponent> COMP getComponent(ComponentType type, Class<COMP> classType) {
		return componentStore.get(type, classType);
	}

	public boolean hasComponent(ComponentType destruction) {
		return hasAllOfAbility(destruction.getMask());
	}

	public void removeComponent(ComponentType type) {
		removeComponent(componentStore.get(type));
	}

	public EntityComponent addComponent(ComponentType type) {
		EntityComponent component = engine.entityFactory.createComponent(type);
		switch (type) {
		case ANGULAR_TRANSFORM:
			angularTransform = (AngularTransformComponent) component;
			break;
		case LINEAR_TRANSFORM:
			linearTransform = (LinearTransformComponent) component;
			break;
		}
		addComponent(component);
		return component;
	}

}
