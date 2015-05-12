package com.emptypockets.spacemania.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.engine.entityManager.BaseEntityManager;
import com.emptypockets.spacemania.engine.entityManager.BoundedEntityManager;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by jenfield on 10/05/2015.
 */
public class GameEngine {

    Rectangle bounds;

    BoundedEntityManager<BaseEntity> manager;

    long startTime;
    long lastUpdate;

    long elapsedTime;
    float deltaTime;

    public GameEngine() {
        manager = new BoundedEntityManager<BaseEntity>();
    }


    public void setupTestData(){
        for (int i = 0; i < 0; i++) {
            BaseEntity entity = new BaseEntity();
            entity.getPos().set(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
            entity.getVel().set(MathUtils.random(-50, 50), MathUtils.random(-50, 50));
            entity.setSize(MathUtils.random(50), MathUtils.random(50));
            entity.setAngVel(MathUtils.random(-90, 90));
            manager.addEntity(entity);
        }
        bounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        manager.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public BaseEntityManager getEntityManager(){
        return manager;
    }
    public Collection<BaseEntity> getEntities(){
        return manager.getEntities();
    }

    public void setStart(){
        startTime = System.currentTimeMillis();
    }
    public void update(){
        elapsedTime = System.currentTimeMillis()-startTime;
        deltaTime = System.currentTimeMillis()-lastUpdate;
        lastUpdate = System.currentTimeMillis();
        float delta = deltaTime/1000f;
        manager.update(delta);
    }

    public BaseEntity getEntityById(int id){
        return manager.getEntity(id);
    }

    public long getTime() {
        return elapsedTime;
    }

    public void addEntity(BaseEntity entity) {
        manager.addEntity(entity);
    }

    public void setTime(long time) {
        this.startTime = System.currentTimeMillis()-time;
    }

    public void removeEntity(int id) {
        manager.removeEntity(id);
    }
}
