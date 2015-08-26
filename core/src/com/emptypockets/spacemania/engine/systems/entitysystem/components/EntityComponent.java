package com.emptypockets.spacemania.engine.systems.entitysystem.components;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.utils.PoolsManager;

public abstract class EntityComponent<DATA_TYPE extends ComponentData> implements Poolable {
	protected GameEntity entity;
	public ComponentType componentType;
	public DATA_TYPE data = null;
	public boolean networkSync = false;

	public EntityComponent(ComponentType componentType) {
		this.componentType = componentType;
	}

	public abstract Class<DATA_TYPE> getDataClass();

	public ComponentType getComponentType() {
		return componentType;
	}

	public DATA_TYPE createData() {
		return PoolsManager.obtain(getDataClass());
	}

	public void setupData() {
		this.data = createData();
	}

	public DATA_TYPE getData() {
		return data;
	}

	public void setEntity(GameEntity entity) {
		this.entity = entity;
	}
	
	public void reset() {
		entity = null;
		networkSync = false;
		PoolsManager.free(data);
		data = null;
	}

	public void setData(DATA_TYPE data) {
		this.data = data;
	}
	
	public boolean dataChanged(DATA_TYPE data){
		return this.data.changed(data);
	}
	public void writeData(DATA_TYPE data){
		this.data.setComponentData(data);
	}
	
	public void readData(DATA_TYPE data){
		this.data.getComponentData(data);
	}
}
