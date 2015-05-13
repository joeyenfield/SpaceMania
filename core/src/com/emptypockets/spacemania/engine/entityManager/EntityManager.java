package com.emptypockets.spacemania.engine.entityManager;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.engine.entityManager.processors.EntityIterator;
import com.emptypockets.spacemania.engine.entityManager.processors.EntityProcessor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jenfield on 10/05/2015.
 */
public class EntityManager<ENT extends BaseEntity> implements Disposable {
    HashMap<Integer, ENT> entities;

    public EntityManager() {
        this.entities = new HashMap<Integer, ENT>();
    }

    public synchronized ENT getEntity(int id) {
        return entities.get(id);
    }

    public synchronized void addEntity(ENT ent) {
        entities.put(ent.getId(), ent);
    }

    public synchronized void processEntities(EntityProcessor<ENT> entityProcessor){
        for(ENT ent : entities.values()){
            entityProcessor.processEntity(ent);
        }
    }

    public synchronized void processEntities(EntityIterator<ENT> entityProcessor){
        Iterator<ENT> iterator = entities.values().iterator();
        entityProcessor.iteratateEntities(iterator);
    }

    public synchronized void removeEntity(int id) {
        entities.remove(id);
    }

    public synchronized  void dispose() {
        releaseAll();
    }

    public synchronized  void releaseAll() {
        entities.clear();
    }
}
