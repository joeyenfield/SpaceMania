package com.emptypockets.spacemania.engine;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.processes.client.ClientNetworkProcess;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntityType;
import com.emptypockets.spacemania.engine.systems.factory.GameEntityFactory;
import com.emptypockets.spacemania.gui.AssetStore;

public class GameEngineClient extends GameEngine {
	
	
	public ClientNetworkProcess clientNetworkProcess = new ClientNetworkProcess();

	public AssetStore assetStore;
	
	public GameEngineClient() {
		super();
		assetStore = new AssetStore();
		entityFactory = new GameEntityFactory(this, assetStore);
		
		ArrayList<EngineProcess> processes = new ArrayList<EngineProcess>();
		processes.add(clientNetworkProcess);
		processes.add(movementProcess);
		
		//Client shouldnt shoot - only take values from the server
//		processes.add(weaponProcess);
		processes.add(partitionProcess);
		processes.add(collissionProcess);
		processes.add(destructionProcess);
		setProcesses(processes);
		
		setName("CLIENT");
	}
	
	public GameEntity createEntity(GameEntityType type){
		throw new RuntimeException("Should not create dump entities");
	}



}