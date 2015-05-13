package com.emptypockets.spacemania.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.engine.entityManager.EntityManager;
import com.emptypockets.spacemania.engine.entityManager.processors.BoundedEntityProcessor;
import com.emptypockets.spacemania.engine.entityManager.processors.DeadEntityCleanerEntityIterator;
import com.emptypockets.spacemania.engine.entityManager.processors.EntityUpdaterProcessor;

/**
 * Created by jenfield on 10/05/2015.
 */
public class GameEngine {

    EntityManager manager;

    //Processors
    EntityUpdaterProcessor entityUpdaterProcessor;
    BoundedEntityProcessor boundedProcessor;
    DeadEntityCleanerEntityIterator deadCleanerIterator;

    boolean running = false;

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
    }


    public void setupTestData() {
        for (int i = 0; i < 0; i++) {
            BaseEntity entity = new BaseEntity();
            entity.getPos().set(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
            entity.getVel().set(MathUtils.random(-50, 50), MathUtils.random(-50, 50));
            entity.setSize(MathUtils.random(50), MathUtils.random(50));
            entity.setAngVel(MathUtils.random(-90, 90));
            manager.addEntity(entity);
        }

        boundedRegion.set(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }


    public void start() {
        startTime = System.currentTimeMillis();
        manager = new EntityManager<BaseEntity>();
        unpause();
    }


    public void stop() {
        pause();
        if(manager != null) {
            manager.releaseAll();
        }
    }

    public void pause(){
        running = false;
    }

    public void unpause(){
        running = true;
    }


    public void update() {
        elapsedTime = System.currentTimeMillis() - startTime;
        deltaTime = System.currentTimeMillis() - lastUpdate;
        lastUpdate = System.currentTimeMillis();
        float delta = deltaTime / 1000f;


        if(running) {
            entityUpdaterProcessor.setDeltaTime(delta);
            boundedProcessor.getRegion().set(boundedRegion);
            manager.processEntities(entityUpdaterProcessor);
            manager.processEntities(boundedProcessor);
            manager.processEntities(deadCleanerIterator);
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

    public void addEntity(BaseEntity entity) {
        manager.addEntity(entity);
    }

    public void removeEntity(int id) {
        manager.removeEntity(id);
    }

    public EntityManager getEntityManager() {
        return manager;
    }

    public void dispose() {
        if(manager != null) {
            manager.dispose();
        }
        manager = null;
        boundedRegion = null;
        boundedProcessor = null;
        entityUpdaterProcessor = null;
    }

    public Rectangle getBoundedRegion() {
        return boundedRegion;
    }
}
