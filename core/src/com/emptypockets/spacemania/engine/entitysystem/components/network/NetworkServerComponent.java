package com.emptypockets.spacemania.engine.entitysystem.components.network;

import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.network.transport.data.engine.entity.ComponentDataStore;
import com.emptypockets.spacemania.network.transport.data.engine.entity.GameEntityNetworkSync;

public class NetworkServerComponent extends EntityComponent<NetworkServerData> {
	public NetworkServerComponent() {
		super(ComponentType.NETWORK_SERVER);
	}

	
	public boolean shouldSendData(ComponentDataStore lastData, GameEntityNetworkSync sync) {
		boolean shouldSend = false;
		for (int i = 0; i < ComponentType.COMPONENT_TYPES; i++) {
			EntityComponent comp = entity.componentStore.component[i];
			// If No compoent
			if (comp == null) {
				// Check was previously there
				if (lastData.data[i] != null) {
					// Notify Component been removed
					lastData.data[i] = null;
					sync.data.put(comp.componentType, null);
					shouldSend = true;
				}
			} else {
				// Chech should be network synced
				if (comp.networkSync) {
					// Was value there
					if (lastData.data[i] == null) {
						lastData.data[i] = comp.createData();
					}
					
					//Check if value changed
					if (comp.data.changed(lastData.data[i])) {
						comp.data.getComponentData(lastData.data[i]);
						sync.data.put(comp.componentType, lastData.data[i]);
						shouldSend = true;
					}

				}
			}
		}
		return shouldSend;
	}

	@Override
	public Class<NetworkServerData> getDataClass() {
		return NetworkServerData.class;
	}

}
