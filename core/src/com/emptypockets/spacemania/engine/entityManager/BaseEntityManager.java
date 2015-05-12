package com.emptypockets.spacemania.engine.entityManager;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.engine.entities.BulletEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jenfield on 10/05/2015.
 */
public class BaseEntityManager<ENT extends BaseEntity> implements Disposable {
    HashMap<Integer, ENT> entities;

    public BaseEntityManager() {
        this.entities = new HashMap<Integer, ENT>();
    }

    public synchronized ENT getEntity(int id) {
        return entities.get(id);
    }

    public synchronized void addEntity(ENT ent) {
        entities.put(ent.getId(), ent);
    }

    public synchronized void update(float timeDelta) {
        Iterator<ENT> entitiesList = entities.values().iterator();
        while (entitiesList.hasNext()) {
            ENT ent = entitiesList.next();
            updateEntity(ent, timeDelta);
            if (ent.isDead()) {
                entitiesList.remove();
            }

        }
    }

    protected void updateEntity(ENT ent, float timeDelta) {
        ent.update(timeDelta);
    }

    public Collection<ENT> getEntities() {
        return entities.values();
    }

    public void removeEntity(int id) {
        entities.remove(id);
    }

    public void dispose() {
        releaseAll();
        entities = null;
    }

    public void releaseAll() {
        entities.clear();
    }
}
