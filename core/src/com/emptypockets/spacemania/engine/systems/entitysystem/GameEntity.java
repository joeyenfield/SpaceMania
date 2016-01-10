package com.emptypockets.spacemania.engine.systems.entitysystem;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponentStore;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.transform.AngularTransformComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.transform.LinearTransformComponent;
import com.emptypockets.spacemania.utils.BitUtilities;

public class GameEntity implements Poolable {

	public int entityId;
	public Bits componentsMask = new Bits();
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
		component.getComponentType().addAbility(componentsMask);
		componentStore.add(component);
		component.setEntity(this);
	}

	public void removeComponent(EntityComponent<?> component) {
		component.getComponentType().removeAbility(componentsMask);
		componentStore.remove(component.getComponentType());
	}

	public EntityComponent<?> getComponent(ComponentType type) {
		return componentStore.get(type);
	}

	public boolean hasAnyOfAbility(Bits abilities) {
		return componentsMask.intersects(abilities);
	}

	public boolean hasAllOfAbility(Bits abilities) {
		return componentsMask.containsAll(abilities);
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
		componentsMask.clear();
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
		// engine.println("(" + entityId + ") : Remove Comp - " + type.name());
		EntityComponent comp = componentStore.get(type);
		if (comp != null) {
			removeComponent(comp);
		} else {
			engine.println("Error : Entity (" + entityId + ") : Missing Type :" + type);
		}
	}

	public EntityComponent addComponent(ComponentType type) {
		// engine.println("(" + entityId + ") : Add Comp  - " + type.name());
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

	public String getDesc() {
		// if (false) {
		// return "(" + entityId + " : " + type.name() + " : " +
		// BitUtilities.toString(componentsMask) + ")";
		// } else {
		return "(" + entityId + ")";
		// }
	}

	public void print(String string) {
		engine.print(getDesc() + ":" + string);
	}

	public void println(String string) {
		engine.println(getDesc() + ":" + string);
	}

	public Vector2 getPos() {
		return linearTransform.state.pos;
	}

	public String getMaskString() {
		return BitUtilities.toString(componentsMask);
	}
}
