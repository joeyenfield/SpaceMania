package com.emptypockets.spacemania.engine.entitysystem.components;

import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class ComponentData<TYPE extends ComponentData<?>> implements Poolable{
	public abstract void getComponentData(TYPE result);
	public abstract void setComponentData(TYPE data);
	public abstract boolean changed(TYPE type);
	public abstract void reset();
}
