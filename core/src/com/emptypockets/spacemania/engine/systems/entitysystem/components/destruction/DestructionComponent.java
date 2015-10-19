package com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class DestructionComponent extends EntityComponent<DestructionState> {

	public DestructionComponent() {
		super(ComponentType.DESTRUCTION);
	}

	@Override
	public void setupState() {
		super.setupState();
		networkSync = true;
	}

	public void update(float deltaTime) {
		if (entity.engine.getTime() > state.destroyTime) {
			state.remove = true;
		}
	}

	@Override
	public Class<DestructionState> getStateClass() {
		return DestructionState.class;
	}

}
