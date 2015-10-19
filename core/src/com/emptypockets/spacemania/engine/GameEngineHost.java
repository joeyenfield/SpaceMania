package com.emptypockets.spacemania.engine;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.processes.host.HostNetworkProcess;
import com.emptypockets.spacemania.engine.systems.factory.HostGameEntityFactory;
import com.emptypockets.spacemania.gui.AssetStore;

public class GameEngineHost extends GameEngine {

	public HostNetworkProcess hostNetworkProcess = new HostNetworkProcess();

	public AssetStore assetStore;

	public GameEngineHost() {
		super();

		assetStore = new AssetStore();
		entityFactory = new HostGameEntityFactory(this, assetStore);

		ArrayList<EngineProcess> processes = new ArrayList<EngineProcess>();
		processes.add(hostNetworkProcess);
		processes.add(movementProcess);
		processes.add(weaponProcess);
		processes.add(partitionProcess);
		processes.add(collissionProcess);
		processes.add(destructionProcess);
		processes.add(aiProcess);
		setProcesses(processes);
		

		setName("HOST");
	}
	
}
