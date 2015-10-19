package com.emptypockets.spacemania.engine.processes.common;

import com.emptypockets.spacemania.engine.EngineProcess;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.systems.entitysystem.GameEntity;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.systems.entitysystem.components.weapon.WeaponComponent;
import com.emptypockets.spacemania.holders.SingleProcessor;

public class WeaponProcess implements SingleProcessor<GameEntity>, EngineProcess<GameEngine> {
	float deltaTime = 0;

	@Override
	public void process(GameEntity entity) {
		entity.getComponent(ComponentType.WEAPON, WeaponComponent.class).update(deltaTime);
	}

	@Override
	public void process(GameEngine engine) {
		this.deltaTime = engine.getDeltaTime();
		engine.entitySystem.process(this, ComponentType.WEAPON);
	}

	@Override
	public void preProcess(GameEngine engine) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postProcess(GameEngine engine) {
		// TODO Auto-generated method stub

	}
}
