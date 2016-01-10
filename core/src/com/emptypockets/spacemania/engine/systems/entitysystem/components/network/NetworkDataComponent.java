package com.emptypockets.spacemania.engine.systems.entitysystem.components.network;

import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentState;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.movement.LinearMovementComponent;
import com.emptypockets.spacemania.network.engine.entity.ComponentDataStore;
import com.emptypockets.spacemania.network.engine.entity.GameEntityNetworkSync;

public class NetworkDataComponent extends EntityComponent<NetworkState> {
	public NetworkDataComponent() {
		super(ComponentType.NETWORK_DATA);
	}

	public boolean readData(ComponentDataStore lastData, GameEntityNetworkSync sync) {

		// entity.engine.println("(" + entity.entityId + ") : Read Sync");
		// for (int i = 0; i < lastData.data.length; i++) {
		// entity.engine.println("(" + entity.entityId + ") : Type Before - (" + ComponentType.getById(i).name() + ") - " + lastData.data[i]);
		// }
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
					boolean first = false;
					// Was value there
					if (lastData.data[i] == null) {
						lastData.data[i] = comp.createState();
						first = true;
					}

					// Check if value changed
					if (first || comp.dataChanged(lastData.data[i])) {
						comp.readData(lastData.data[i]);
						sync.data.put(comp.componentType, lastData.data[i]);

						// If Vel Change also send Position -
						// This fixes an offset issue on client side when the position stops moving between syncs
						if (comp.componentType == ComponentType.LINEAR_MOVEMENT) {
							LinearMovementComponent currentLinearMovementComp = (LinearMovementComponent) comp;
							if (currentLinearMovementComp.state.vel.len2() < 1) {
								if (!sync.data.containsKey(ComponentType.LINEAR_TRANSFORM)) {
									// Linear Transform is 0
									int linearTransformId = ComponentType.LINEAR_TRANSFORM.id;
									EntityComponent linearComp = entity.componentStore.component[linearTransformId];
									linearComp.readData(lastData.data[linearTransformId]);
									sync.data.put(ComponentType.LINEAR_TRANSFORM, lastData.data[linearTransformId]);
								}
							}
						}

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
		// for (int i = 0; i < lastData.data.length; i++) {
		// entity.engine.println("(" + entity.entityId + ") : Type After - (" + ComponentType.getById(i).name() + ") - " + lastData.data[i]);
		// }
		// entity.engine.println("("+entity.entityId+") : Write Sync - send"+shouldSend+" : "+sync.data.size());
		return shouldSend;
	}

	public void writeData(GameEntityNetworkSync sync) {
		// entity.engine.println("("+entity.entityId+") : Write Sync"+sync.data.size());
		for (ComponentType type : sync.data.keySet()) {
			ComponentState data = sync.data.get(type);
			// entity.engine.println("("+entity.entityId+") : Write type - "+type.name()+" : "+data);
			if (data == null) {
				// Component Removed
				entity.removeComponent(type);
			} else {
				// Ensure has
				EntityComponent comp = entity.componentStore.get(type);
				if (comp == null) {
					comp = entity.addComponent(type);
				}
				// entity.engine.println("(" + entity.entityId + ") : Update" + type.name());
				comp.writeData(data);
			}

		}
	}

	@Override
	public Class<NetworkState> getStateClass() {
		return NetworkState.class;
	}

}
