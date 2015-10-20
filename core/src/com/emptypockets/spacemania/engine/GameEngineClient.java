package com.emptypockets.spacemania.engine;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.processes.client.ClientNetworkProcess;
import com.emptypockets.spacemania.engine.processes.client.NetworkLinearTransformFixer;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.factory.ClientEntityFactory;
import com.emptypockets.spacemania.gui.AssetStore;

public class GameEngineClient extends GameEngine {

	public ClientNetworkProcess clientNetworkProcess = new ClientNetworkProcess();
	public NetworkLinearTransformFixer clientLinearTransformFixer = new NetworkLinearTransformFixer();
	public AssetStore assetStore;

	public GameEngineClient() {
		super();
		assetStore = new AssetStore();
		entityFactory = new ClientEntityFactory(this, assetStore);

		ArrayList<EngineProcess> processes = new ArrayList<EngineProcess>();
		processes.add(clientNetworkProcess);
		processes.add(clientLinearTransformFixer);
		processes.add(movementProcess);
		processes.add(partitionProcess);
		processes.add(destructionProcess);
//		processes.add(aiProcess);
		setProcesses(processes);

		setName("CLIENT");
	}

	public GameEntity createEntity(GameEntityType type) {
		throw new RuntimeException("Should not create dump entities");
	}
	
}