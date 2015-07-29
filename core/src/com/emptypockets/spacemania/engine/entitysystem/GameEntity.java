package com.emptypockets.spacemania.engine.entitysystem;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.AngularTransformComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.LinearTransformComponent;
import com.emptypockets.spacemania.utils.BitUtilities;
import com.emptypockets.spacemania.utils.PoolsManager;

public class GameEntity implements Poolable {

	public int entityId;
	public int componentsMask;

	ArrayList<EntityComponent<?>> components = new ArrayList<EntityComponent<?>>(10);
	public HashMap<ComponentType, EntityComponent<?>> componentsMap = new HashMap<ComponentType, EntityComponent<?>>(100);
	public LinearTransformComponent linearTransform;
	public AngularTransformComponent angularTransform;
	public GameEngine engine;

	public GameEntity() {
	}

	public void setData(GameEngine engine, int entityId) {
		this.entityId = entityId;
		this.engine = engine;
	}

	public void addComponent(EntityComponent<?> component) {
		componentsMask = component.getComponentType().addAbility(componentsMask);
		componentsMap.put(component.getComponentType(), component);
		components.add(component);
		component.setEntity(this);
	}

	public void removeComponent(EntityComponent<?> component) {
		componentsMask = component.getComponentType().removeAbility(componentsMask);
		components.remove(componentsMap.remove(component.getComponentType()));
	}

	public EntityComponent<?> getComponent(ComponentType type) {
		return componentsMap.get(type);
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
	public void reset() {
		engine = null;
		entityId = -1;
		componentsMap.clear();
		linearTransform = null;
		angularTransform = null;
		componentsMask = 0;
		int size = components.size();
		for (int i = 0; i < size; i++) {
			PoolsManager.free(components.get(i));
		}
		components.clear();
	}

}
