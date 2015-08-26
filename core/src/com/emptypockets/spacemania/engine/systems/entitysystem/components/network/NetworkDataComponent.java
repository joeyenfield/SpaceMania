package com.emptypockets.spacemania.engine.systems.entitysystem.components.network;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentData;
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
		sync.entityId = entity.entityId;
		for (int i = 0; i < ComponentType.COMPONENT_TYPES; i++) {
			EntityComponent comp = entity.componentStore.component[i];
			// If No compoent
			if (comp == null) {
				// Check was previously there
				if (lastData.data[i] != null) {
					// Notify Component been removed
					lastData.data[i] = null;
					sync.data.put(ComponentType.getById(i), null);
					shouldSend = true;
				}
			} else {
				// Chech should be network synced
				if (comp.networkSync) {
					// Was value there
					if (lastData.data[i] == null) {
						lastData.data[i] = comp.createData();
					}

					// Check if value changed
					if (comp.dataChanged(lastData.data[i])) {
						comp.readData(lastData.data[i]);
						sync.data.put(comp.componentType, lastData.data[i]);
						// entity.engine.println("("+entity.entityId+") : CHANGE : "+comp.componentType.name());
						shouldSend = true;
					} else {
						// entity.engine.println("("+entity.entityId+") : SAME   : "+comp.componentType.name());
					}

				} else {
					// entity.engine.println("("+entity.entityId+") : NOT SYC  : "+comp.componentType.name());
				}
			}
		}
		return shouldSend;
	}

	public void writeData(GameEntityNetworkSync sync) {
		for (ComponentType type : sync.data.keySet()) {
			ComponentData data = sync.data.get(type);
			if (data == null) {
				// Component Removed
				entity.removeComponent(type);
				// entity.engine.println("(" + entity.entityId + ") : Remove" + type.name());
			} else {
				// Ensure has
				EntityComponent comp = entity.componentStore.get(type);
				if (comp == null) {
					// entity.engine.println("(" + entity.entityId + ") : NEW" + type.name());
					comp = entity.addComponent(type);
				}
				// entity.engine.println("(" + entity.entityId + ") : Update" + type.name());
				comp.writeData(data);
			}

		}
	}

	@Override
	public Class<NetworkData> getDataClass() {
		return NetworkData.class;
	}

}
