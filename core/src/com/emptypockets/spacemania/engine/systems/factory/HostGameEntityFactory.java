package com.emptypockets.spacemania.engine.systems.factory;

import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ai.AiComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.transform.ClientLinearTransformComponent;
import com.emptypockets.spacemania.gui.AssetStore;
import com.emptypockets.spacemania.gui.screens.GameEngineScreen;
import com.emptypockets.spacemania.utils.PoolsManager;

public class HostGameEntityFactory extends GameEntityFactory{

	public HostGameEntityFactory(GameEngine engine, AssetStore assetStore) {
		super(engine, assetStore);
	}

	@Override
	protected GameEntity createDefenceShipEntity(int entityId) {
		GameEntity ents = super.createDefenceShipEntity(entityId);
		return ents;
	}
}
