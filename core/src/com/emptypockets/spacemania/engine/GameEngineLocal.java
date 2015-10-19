package com.emptypockets.spacemania.engine;

import java.util.ArrayList;

import com.emptypockets.spacemania.engine.systems.factory.HostGameEntityFactory;
import com.emptypockets.spacemania.gui.AssetStore;

public class GameEngineLocal extends GameEngine {

	public AssetStore assetStore;

	public GameEngineLocal() {
		super();

		assetStore = new AssetStore();
		entityFactory = new HostGameEntityFactory(this, assetStore);

		ArrayList<EngineProcess> processes = new ArrayList<EngineProcess>();
		processes.add(movementProcess);
		processes.add(weaponProcess);
		processes.add(partitionProcess);
		processes.add(collissionProcess);
		processes.add(destructionProcess);
		setProcesses(processes);

	}
}