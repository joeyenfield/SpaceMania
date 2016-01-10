package com.emptypockets.spacemania.engine.network.client;

import java.util.ArrayList;

import com.emptypockets.metrics.plotter.DataLogger;
import com.emptypockets.spacemania.engine.GameEngineClient;
import com.emptypockets.spacemania.engine.input.PlayerInputData;
import com.emptypockets.spacemania.engine.input.PlayerInputProducer;
import com.emptypockets.spacemania.engine.network.host.HostPlayerAdapter;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.controls.ControlComponent;
import com.emptypockets.spacemania.network.engine.GameEngineState;
import com.emptypockets.spacemania.utils.KryoUtils;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ClientPlayerAdapter {
	int myPlayerId;
	PlayerInputProducer inputProducer;

	ClientDataSyncManager clientDataSyncManager = new ClientDataSyncManager();
	ArrayList<GameEngineState> states = new ArrayList<GameEngineState>();

	public HostPlayerAdapter adapter;

	public synchronized void processIncomming(GameEngineClient clientEngine) {
		int size = states.size();
		for (int i = 0; i < size; i++) {
			if (DataLogger.isEnabled()) {
				DataLogger.log(clientEngine.getName() + "-net", 1);
			}
			GameEngineState lastState = states.get(i);
			myPlayerId = lastState.myPlayerId;
			clientDataSyncManager.apply(clientEngine, lastState);
			PoolsManager.free(lastState);
		}
		states.clear();
	}

	private void updateClientSidePlayer(GameEngineClient gameEngine) {
		if (inputProducer != null) {
			PlayerInputData input = inputProducer.getPlayerInput();

			// Update the local player
			GameEntity ent = gameEngine.getEntityById(myPlayerId);
			if (ent != null) {
				if (ent.hasComponent(ComponentType.CONTROL)) {
					ControlComponent cont = ent.getComponent(ComponentType.CONTROL, ControlComponent.class);
					cont.state.shooting = input.shoot;
					cont.state.move.set(input.move);
					cont.state.shootDir.set(input.shootDir);
				}
			}
		}
	}

	public synchronized void recieve(GameEngineState state) {
		states.add(state);
	}

	public synchronized void send(Object o) {
		if (adapter != null) {
			adapter.recieve(KryoUtils.clone(o));
		}
	}

	public void update(GameEngineClient clientEngine) {
		if (inputProducer != null) {
			inputProducer.update();
		}
		updateClientSidePlayer(clientEngine);
	}

	public void setInputProducer(PlayerInputProducer inputProducer) {
		this.inputProducer = inputProducer;
	}

	public void processOutgoing(GameEngineClient gameEngine) {
		if (inputProducer != null) {
			send(inputProducer.getPlayerInput());
		}
	}

}
