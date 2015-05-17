package com.emptypockets.spacemania.engine.entities.manager;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.holders.ObjectProcessor;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jenfield on 10/05/2015.
 */
public class EntityManager<ENT extends BaseEntity> extends ObjectProcessor<ENT> implements Disposable {
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

    public synchronized void removeEntity(int id) {
        entities.remove(id);
    }

    public synchronized void dispose() {
        releaseAll();
    }

    public synchronized void releaseAll() {
        entities.clear();
    }

    @Override
    protected synchronized Iterator<ENT> getIterator() {
        return entities.values().iterator();
    }
}
