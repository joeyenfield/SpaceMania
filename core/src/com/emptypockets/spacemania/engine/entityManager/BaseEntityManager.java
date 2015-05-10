package com.emptypockets.spacemania.engine.entityManager;

import com.badlogic.gdx.math.Intersector;
import com.emptypockets.spacemania.engine.BaseEntity;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by jenfield on 10/05/2015.
 */
public class BaseEntityManager<ENT extends BaseEntity> {
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
        for (ENT ent : entities.values()) {
            updateEntity(ent, timeDelta);
        }
    }

    protected void updateEntity(ENT ent, float timeDelta){
        ent.update(timeDelta);
    }

    public Collection<ENT> getEntities() {
        return entities.values();
    }
}
