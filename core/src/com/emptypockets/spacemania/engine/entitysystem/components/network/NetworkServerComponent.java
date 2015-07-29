package com.emptypockets.spacemania.engine.entitysystem.components.network;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;

public class NetworkServerComponent extends EntityComponent<NetworkServerData> {
	public NetworkServerComponent() {
		super(ComponentType.NETWORK_SERVER);
	}

	public void update(float deltaTime) {
	}

	@Override
	public Class<NetworkServerData> getDataClass() {
		return NetworkServerData.class;
	}

}
