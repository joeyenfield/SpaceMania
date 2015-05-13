package com.emptypockets.spacemania.engine.entityManager.processors;

import com.emptypockets.spacemania.engine.entities.BaseEntity;

import java.util.Iterator;

/**
 * Created by jenfield on 13/05/2015.
 */
public interface EntityIterator<ENT extends BaseEntity> {

    public void iteratateEntities(Iterator<ENT> entities);
}
