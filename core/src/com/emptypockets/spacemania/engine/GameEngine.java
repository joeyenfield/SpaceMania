package com.emptypockets.spacemania.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.entityManager.BaseEntityManager;
import com.emptypockets.spacemania.engine.entityManager.BoundedEntityManager;

import java.util.Collection;

/**
 * Created by jenfield on 10/05/2015.
 */
public class GameEngine {

    Rectangle bounds;

    BoundedEntityManager<com.emptypockets.spacemania.engine.entities.BaseEntity> manager;

    boolean running = false;

    long startTime;
    long lastUpdate;

    long elapsedTime;
    float deltaTime;

    public GameEngine() {
    }


    public void setupTestData() {
        for (int i = 0; i < 0; i++) {
            com.emptypockets.spacemania.engine.entities.BaseEntity entity = new com.emptypockets.spacemania.engine.entities.BaseEntity();
            entity.getPos().set(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
            entity.getVel().set(MathUtils.random(-50, 50), MathUtils.random(-50, 50));
            entity.setSize(MathUtils.random(50), MathUtils.random(50));
            entity.setAngVel(MathUtils.random(-90, 90));
            manager.addEntity(entity);
        }
        bounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        manager.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }


    public void start() {
        startTime = System.currentTimeMillis();
        manager = new BoundedEntityManager<com.emptypockets.spacemania.engine.entities.BaseEntity>();
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
            manager.update(delta);
        }
    }

    public com.emptypockets.spacemania.engine.entities.BaseEntity getEntityById(int id) {
        return manager.getEntity(id);
    }

    public long getTime() {
        return elapsedTime;
    }

    public void setTime(long time) {
        this.startTime = System.currentTimeMillis() - time;
    }

    public void addEntity(com.emptypockets.spacemania.engine.entities.BaseEntity entity) {
        manager.addEntity(entity);
    }

    public void removeEntity(int id) {
        manager.removeEntity(id);
    }

    public BaseEntityManager getEntityManager() {
        return manager;
    }

    public Collection<com.emptypockets.spacemania.engine.entities.BaseEntity> getEntities() {
        return manager.getEntities();
    }

    public void dispose() {
        if(manager != null) {
            manager.dispose();
        }
        manager = null;
    }
}
