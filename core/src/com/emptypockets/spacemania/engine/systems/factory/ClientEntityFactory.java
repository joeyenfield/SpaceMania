package com.emptypockets.spacemania.engine.systems.factory;

import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.EntityComponent;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.transform.ClientLinearTransformComponent;
import com.emptypockets.spacemania.gui.AssetStore;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ClientEntityFactory extends GameEntityFactory{

	public ClientEntityFactory(GameEngine engine, AssetStore assetStore) {
		super(engine, assetStore);
	}

	@Override
	public EntityComponent createComponent(ComponentType type) {
		if(type == ComponentType.LINEAR_TRANSFORM){
			ClientLinearTransformComponent result =  PoolsManager.obtain(ClientLinearTransformComponent.class);
			result.setupState();
			return result;
		}
		return super.createComponent(type);
	}
	
	@Override
	protected GameEntity createDefenceShipEntity(int entityId) {
		GameEntity ents = super.createDefenceShipEntity(entityId);
		return ents;
	}
}
