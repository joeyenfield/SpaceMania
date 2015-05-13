package com.emptypockets.spacemania.engine.entityManager.processors;

import com.emptypockets.spacemania.engine.entities.BaseEntity;

import java.util.Iterator;

/**
 * Created by jenfield on 13/05/2015.
 */
public class DeadEntityCleanerEntityIterator implements EntityIterator<BaseEntity> {
    @Override
    public void iteratateEntities(Iterator<BaseEntity> entities) {
        while (entities.hasNext()) {
            BaseEntity ent = entities.next();
            if (ent.isDead()) {
                entities.remove();
            }
        }
    }
}
