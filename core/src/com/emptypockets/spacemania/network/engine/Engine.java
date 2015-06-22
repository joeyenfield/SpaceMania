package com.emptypockets.spacemania.network.engine;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.partitioning.cell.CellSpacePartition;

public class Engine implements Disposable {
	Rectangle region = new Rectangle();
	EntityManager entities;
	CellSpacePartition entitySpatialPartition;
	long startTime;
	long lastUpdate;
	ArrayList<EngineRegionListener> regionListeners;
	
	public Engine() {
		float size = 4000;
		entities = new EntityManager();
		regionListeners = new ArrayList<EngineRegionListener>();
		entitySpatialPartition = new CellSpacePartition();
		entitySpatialPartition.create(20, 20);
		addRegionListener(entitySpatialPartition);
		setRegion(size);
		start();
	}
	
	public void addRegionListener(EngineRegionListener listener){
		synchronized (regionListeners) {
			regionListeners.add(listener);
		}
	}
	
	public void notifyRegionChanged(){
		synchronized (regionListeners) {
			for(EngineRegionListener list : regionListeners){
				list.notifyRegionChanged(region);
			}
		}
	}

	public void setRegion(float size) {
		setRegion(-size, -size, 2 * size, 2 * size);
	}

	public void setRegion(float x, float y, float wide, float high) {
		region.set(x, y, wide, high);
		notifyRegionChanged();
	}

	public EntityManager getEntityManager() {
		return entities;
	}

	public long getTime() {
		return System.currentTimeMillis() - startTime;
	}

	public long getEngineLastUpdateTime() {
		return lastUpdate;
	}

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public void updateEntities(final float deltaTime) {
		updateEntities(entities, deltaTime);
	}

	public void updateEntities(EntityManager manager, final float deltaTime) {
		manager.process(new SingleProcessor<Entity>() {
			@Override
			public void process(Entity entity) {
				entity.update(deltaTime);
				Vector2 pos = entity.getPos();
				float rad = entity.getRadius();
				float inset = rad + 2;

				boolean hitWall = false;
				if (pos.x - rad < region.x) {
					entity.setPos(region.x + inset, pos.y);
					if (entity.isBounceOffWall()) {
						entity.getVel().x *= -1;
					} else {
						entity.getVel().x = 0;
					}
					hitWall = true;
				}
				if (pos.x + rad > region.x + region.width) {
					entity.setPos(region.x + region.width - inset, pos.y);
					if (entity.isBounceOffWall()) {
						entity.getVel().x *= -1;
					} else {
						entity.getVel().x = 0;
					}
					hitWall = true;
				}
				if (pos.y - rad < region.y) {
					entity.setPos(pos.x, region.y + inset);
					if (entity.isBounceOffWall()) {
						entity.getVel().y *= -1;
					} else {
						entity.getVel().y = 0;
					}
					hitWall = true;
				}
				if (pos.y + rad > region.y + region.height) {
					entity.setPos(pos.x, region.y + region.height - inset);
					if (entity.isBounceOffWall()) {
						entity.getVel().y *= -1;
					} else {
						entity.getVel().y = 0;
					}
					hitWall = true;
				}

				if (hitWall && entity.getType() == EntityType.Bullet) {
					entity.setAlive(false);
				}
			}
		});

		entitySpatialPartition.rebuild(getEntityManager());
	}

	public void update() {
		long time = getTime();
		long delta = time - lastUpdate;
		lastUpdate = time;
		float deltaTime = delta / 1000f;

		updateEntities(deltaTime);

	}

	public void dispose() {

	}

	public void setTime(long time) {
		startTime = System.currentTimeMillis() - time;
	}

	public Rectangle getRegion() {
		return region;
	}

	public CellSpacePartition getEntitySpatialPartition() {
		return entitySpatialPartition;
	}

}
