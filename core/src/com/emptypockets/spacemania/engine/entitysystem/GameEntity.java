package com.emptypockets.spacemania.engine.entitysystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.AngularTransformComponent;
import com.emptypockets.spacemania.engine.entitysystem.components.transform.LinearTransformComponent;
import com.emptypockets.spacemania.utils.BitUtilities;

public class GameEntity {

	public int entityId;
	public int componentsMask;

	ArrayList<EntityComponent<?>> components = new ArrayList<EntityComponent<?>>();

	public HashMap<ComponentType, EntityComponent<?>> componentsMap = new HashMap<ComponentType, EntityComponent<?>>();
	public LinearTransformComponent linearTransform;
	public AngularTransformComponent angularTransform;
	public GameEngine engine;

	public GameEntity(GameEngine engine, int entityId) {
		this.entityId = entityId;
		this.engine = engine;
	}

	public void addComponent(EntityComponent<?> component) {
		componentsMask = component.getComponentType().addAbility(componentsMask);
		componentsMap.put(component.getComponentType(), component);
		components.add(component);
		component.setEntity(this);
		Collections.sort(components);
	}

	public void removeComponent(EntityComponent<?> component) {
		componentsMask = component.getComponentType().removeAbility(componentsMask);
		components.remove(componentsMap.remove(component.getComponentType()));
		Collections.sort(components);
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

	public void update(float deltaTime) {
		int size = components.size();
		for (int i = 0; i < size; i++) {
			components.get(i).update(deltaTime);
		}
	}

	public String getMaskString() {
		return BitUtilities.toString(componentsMask);
	}

}
