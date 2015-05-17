package com.emptypockets.spacemania.engine.entities.manager.processors;

import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.holders.IteratorProcessor;

import java.util.Iterator;

/**
 * Created by jenfield on 13/05/2015.
 */
public class DeadEntityCleanerEntityIterator implements IteratorProcessor<BaseEntity> {
    @Override
    public void process(Iterator<BaseEntity> entities) {
        while (entities.hasNext()) {
            BaseEntity ent = entities.next();
            if (ent.isDead()) {
                entities.remove();
            }
        }
    }
}
