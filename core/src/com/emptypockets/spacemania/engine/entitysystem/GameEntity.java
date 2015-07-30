package com.emptypockets.spacemania.engine.entitysystem;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponentStore;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.AngularTransformComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.LinearTransformComponent;
import com.emptypockets.spacemania.utils.BitUtilities;

public class GameEntity implements Poolable {

	public int entityId;
	public int componentsMask;

	public EntityComponentStore components;
	public LinearTransformComponent linearTransform;
	public AngularTransformComponent angularTransform;
	public GameEngine engine;

	public GameEntity() {
		components = new EntityComponentStore();
	}

	public void setData(GameEngine engine, int entityId) {
		this.entityId = entityId;
		this.engine = engine;
	}

	public void addComponent(EntityComponent<?> component) {
		componentsMask = component.getComponentType().addAbility(componentsMask);
		components.add(component);
		component.setEntity(this);
	}

	public void removeComponent(EntityComponent<?> component) {
		componentsMask = component.getComponentType().removeAbility(componentsMask);
		components.remove(component.getComponentType());
	}

	public EntityComponent<?> getComponent(ComponentType type) {
		return components.get(type);
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
	public int hashCode() {
		return entityId;
	}

	@Override
	public void reset() {
		engine = null;
		entityId = -1;
		components.clear();
		linearTransform = null;
		angularTransform = null;
		componentsMask = 0;
	}

	public <COMP extends EntityComponent> COMP getComponent(ComponentType type, Class<COMP> classType) {
		return components.get(type, classType);
	}

	public boolean hasComponent(ComponentType destruction) {
		return hasAllOfAbility(destruction.getMask());
	}

}
