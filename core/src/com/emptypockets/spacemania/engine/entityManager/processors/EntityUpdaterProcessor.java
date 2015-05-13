package com.emptypockets.spacemania.engine.entityManager.processors;

import com.emptypockets.spacemania.engine.entities.BaseEntity;

/**
 * Created by jenfield on 12/05/2015.
 */
public class EntityUpdaterProcessor implements EntityProcessor<BaseEntity> {

    float deltaTime = 0;

    @Override
    public void processEntity(BaseEntity entity) {
        entity.update(getDeltaTime());
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }
}
