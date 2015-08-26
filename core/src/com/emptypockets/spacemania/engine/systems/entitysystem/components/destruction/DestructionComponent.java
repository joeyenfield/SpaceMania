package com.emptypockets.spacemania.engine.systems.entitysystem.components.destruction;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;

public class DestructionComponent extends EntityComponent<DestructionData> {

	public DestructionComponent() {
		super(ComponentType.DESTRUCTION);
	}

	@Override
	public void setupData() {
		super.setupData();
		networkSync = true;
	}

	public void update(float deltaTime) {
		if (entity.engine.getTime() > data.destroyTime) {
			data.remove = true;
		}
	}

	@Override
	public Class<DestructionData> getDataClass() {
		return DestructionData.class;
	}

}
