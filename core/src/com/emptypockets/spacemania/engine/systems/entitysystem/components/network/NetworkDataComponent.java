package com.emptypockets.spacemania.engine.systems.entitysystem.components.network;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.network.common.data.engine.entity.ComponentDataStore;
import com.emptypockets.spacemania.network.common.data.engine.entity.GameEntityNetworkSync;

public class NetworkDataComponent extends EntityComponent<NetworkData> {
	public NetworkDataComponent() {
		super(ComponentType.NETWORK_DATA);
	}

	
	public boolean readData(ComponentDataStore lastData, GameEntityNetworkSync sync) {
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
	
	public void writeData(GameEntityNetworkSync sync){
		for (int i = 0; i < ComponentType.COMPONENT_TYPES; i++) {
			
		}
	}

	@Override
	public Class<NetworkData> getDataClass() {
		return NetworkData.class;
	}

}
