package com.emptypockets.spacemania.network.engine;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.grid.typeB.GridData2D;

public class Engine implements Disposable{
	EntityManager entities;
	long startTime;
	long lastUpdate;
	GridData2D gridData;
	
	public Engine() {
		entities = new EntityManager();
		start();
	}

	public EntityManager getEntityManager() {
		return entities;
	}

	public long getTime(){
		return System.currentTimeMillis()-startTime;
	}
	
	public long getEngineLastUpdateTime(){
		return lastUpdate;
	}

	public void start(){
		startTime = System.currentTimeMillis();
	}
	public void update() {
		long time = getTime();
		long delta = time-lastUpdate;
		lastUpdate = time;
		final float deltaTime = delta/1000f;
		entities.process(new SingleProcessor<Entity>() {
			@Override
			public void process(Entity entity) {
				entity.update(deltaTime);
			}
		});
	}

	public void dispose() {
		
	}

	public void setTime(long time) {
		startTime=System.currentTimeMillis()-time;
	}
}
