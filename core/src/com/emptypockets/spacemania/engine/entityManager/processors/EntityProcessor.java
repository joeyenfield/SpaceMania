package com.emptypockets.spacemania.engine.entityManager.processors;

import com.emptypockets.spacemania.engine.entities.BaseEntity;

/**
 * Created by jenfield on 12/05/2015.
 */
public interface EntityProcessor<ENT extends BaseEntity> {
    public void processEntity(ENT entity);
}
