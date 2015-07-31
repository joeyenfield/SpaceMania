package com.emptypockets.spacemania.engine.entitysystem.components.destruction;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

public class DestructionComponent extends EntityComponent<DestructionData> {

	public DestructionComponent() {
		super(ComponentType.DESTRUCTION);
	}

	public void update(float deltaTime) {
		if (System.currentTimeMillis() > data.destroyTime) {
			data.remove = true;
		}
	}

	@Override
	public Class<DestructionData> getDataClass() {
		return DestructionData.class;
	}

}