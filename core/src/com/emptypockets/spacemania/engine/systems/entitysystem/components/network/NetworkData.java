package com.emptypockets.spacemania.engine.systems.entitysystem.components.network;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentData;

public class NetworkData extends ComponentData<NetworkData> {

	boolean first = false;
	
	@Override
	public void getComponentData(NetworkData result) {
	}

	@Override
	public void setComponentData(NetworkData data) {
	}

	@Override
	public boolean changed(NetworkData data) {
		return false;
	}

	@Override
	public void reset() {
		first = false;
	}

}
