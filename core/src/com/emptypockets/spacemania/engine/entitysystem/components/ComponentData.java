package com.emptypockets.spacemania.engine.entitysystem.components;

public abstract class ComponentData<TYPE extends ComponentData<?>> {
	public abstract void getComponentData(TYPE result);
	public abstract void setComponentData(TYPE data);
	public abstract boolean changed(TYPE type);
}
