package com.emptypockets.spacemania.engine.systems.entitysystem.components.network;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;

public class NetworkState extends ComponentState<NetworkState> {

	boolean first = false;
	
	@Override
	public void readComponentState(NetworkState result) {
	}

	@Override
	public void writeComponentState(NetworkState data) {
	}

	@Override
	public boolean hasStateChanged(NetworkState data) {
		return false;
	}

	@Override
	public void reset() {
		first = false;
	}

}
