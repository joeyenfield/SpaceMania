package com.emptypockets.spacemania.engine.entitysystem;

import com.badlogic.gdx.math.MathUtils;
import com.emptypockets.spacemania.engine.entitysystem.components.ComponentType;
import com.emptypockets.spacemania.engine.entitysystem.components.movement.LinearMovementData;
import com.emptypockets.spacemania.utils.event.Event;

public class Tester {
	public static void main(String[] args) {
		EntityFactory factory = new EntityFactory();

		EntitySystem entitySystem = new EntitySystem();

		// Create entities
		int entityCount = 1000000;
		Event createTime = new Event();
		createTime.start();
		for (int i = 0; i < entityCount; i++) {
			entitySystem.add(factory.createEntity(i));
		}
		createTime.end();
		System.out.println("Creation Time : " + createTime.getSeconds() * 1000);

		// Update Velocities
		Event setVelTime = new Event();
		setVelTime.start();
		for (int i = 0; i < entityCount; i++) {
			((LinearMovementData) entitySystem.getEntityById(i).getComponent(ComponentType.LINEAR_MOVEMENT).data).vel.x = MathUtils.random();
		}
		setVelTime.end();
		System.out.println("Set Vel Time : " + setVelTime.getSeconds() * 1000);

		for (int i = 0; i < 100; i++) {
			Event updateTime = new Event();
			updateTime.start();
			entitySystem.update(1f);
			updateTime.end();
			System.out.println("Update Time : " + updateTime.getSeconds() * 1000);
		}
	}
}
