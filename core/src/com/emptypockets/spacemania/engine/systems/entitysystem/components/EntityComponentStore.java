package com.emptypockets.spacemania.engine.systems.entitysystem.components;

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
import com.emptypockets.spacemania.utils.PoolsManager;

public class EntityComponentStore {

	public EntityComponent[] component = ComponentType.getComponentHolder();

	public <TYPE extends EntityComponent> TYPE get(ComponentType type, Class<TYPE> classType) {
		return (TYPE) component[type.id];
	}

	public EntityComponent get(ComponentType type) {
		return component[type.id];
	}

	public void add(EntityComponent compData) {
		component[compData.componentType.id] = compData;
	}

	public void remove(ComponentType type) {
		PoolsManager.free(component[type.id]);
		component[type.id] = null;
	}


	public void clear() {
		for (int i = 0; i < component.length; i++) {
			if (component[i] != null) {
				PoolsManager.free(component[i]);
				component[i] = null;
			}
		}
	}
}
