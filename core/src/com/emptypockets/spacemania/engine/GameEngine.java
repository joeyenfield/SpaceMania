package com.emptypockets.spacemania.engine;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.engine.entities.manager.EntityManager;
import com.emptypockets.spacemania.engine.entities.manager.processors.BoundedEntityProcessor;
import com.emptypockets.spacemania.engine.entities.manager.processors.DeadEntityCleanerEntityIterator;
import com.emptypockets.spacemania.engine.entities.manager.processors.EntityUpdaterProcessor;

/**
 * Created by jenfield on 10/05/2015.
 */
public class GameEngine {

    EntityManager manager;

    //Processors
    EntityUpdaterProcessor entityUpdaterProcessor;
    BoundedEntityProcessor boundedProcessor;
    DeadEntityCleanerEntityIterator deadCleanerIterator;

    boolean engineRunning = false;

    long startTime;
    long lastUpdate;

    long elapsedTime;
    float deltaTime;

    Rectangle boundedRegion;

    public GameEngine() {
        entityUpdaterProcessor = new EntityUpdaterProcessor();
        boundedProcessor = new BoundedEntityProcessor();
        deadCleanerIterator = new DeadEntityCleanerEntityIterator();
        boundedRegion = new Rectangle();
        manager = new EntityManager<BaseEntity>();
    }

    public void dispose() {
        if (manager != null) {
            manager.dispose();
        }
        manager = null;
        boundedRegion = null;
        boundedProcessor = null;
        entityUpdaterProcessor = null;
    }

    public synchronized void start() {
        startTime = System.currentTimeMillis();
        unpause();
    }

    public synchronized void stop() {
        pause();
        manager.releaseAll();
    }

    public synchronized void pause() {
        engineRunning = false;
    }

    public synchronized void unpause() {
        engineRunning = true;
    }

    public synchronized void update() {
        elapsedTime = System.currentTimeMillis() - startTime;
        deltaTime = System.currentTimeMillis() - lastUpdate;
        lastUpdate = System.currentTimeMillis();
        float delta = deltaTime / 1000f;

        if (engineRunning) {
            entityUpdaterProcessor.setDeltaTime(delta);
            boundedProcessor.getRegion().set(boundedRegion);
            manager.process(entityUpdaterProcessor);
            manager.process(boundedProcessor);
            manager.process(deadCleanerIterator);
        }
    }

    public BaseEntity getEntityById(int id) {
        return manager.getEntity(id);
    }

    public long getTime() {
        return elapsedTime;
    }

    public void setTime(long time) {
        this.startTime = System.currentTimeMillis() - time;
    }

    public synchronized void addEntity(BaseEntity entity) {
        manager.addEntity(entity);
    }

    public synchronized void removeEntity(int id) {
        manager.removeEntity(id);
    }

    public EntityManager getEntityManager() {
        return manager;
    }


    public Rectangle getBoundedRegion() {
        return boundedRegion;
    }
}
