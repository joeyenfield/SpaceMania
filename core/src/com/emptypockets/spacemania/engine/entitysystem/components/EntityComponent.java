package com.emptypockets.spacemania.engine.entitysystem.components;

import com.emptypockets.spacemania.engine.entitysystem.GameEntity;

public abstract class EntityComponent<DATA_TYPE extends ComponentData<?>> implements Comparable<EntityComponent<?>> {
	protected GameEntity entity;
	public ComponentType componentType;
	public DATA_TYPE data;

	public EntityComponent(ComponentType componentType, DATA_TYPE data) {
		this.componentType = componentType;
		this.data = data;
	}

	public abstract void update(float deltaTime);

	public abstract void reset();

	public ComponentType getComponentType() {
		return componentType;
	}

	public DATA_TYPE getData() {
		return data;
	}

	@Override
	public int compareTo(EntityComponent<?> o) {
		return componentType.updateOrder - o.componentType.updateOrder;
	}

	public void setEntity(GameEntity entity) {
		this.entity = entity;
		reset();
		data.reset();
	}
}
