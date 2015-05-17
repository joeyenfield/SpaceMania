package com.emptypockets.spacemania.engine.entities.manager.processors;

import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.holders.SingleProcessor;

/**
 * Created by jenfield on 12/05/2015.
 */
public class EntityUpdaterProcessor implements SingleProcessor<BaseEntity> {

    float deltaTime = 0;

    @Override
    public void process(BaseEntity entity) {
        entity.update(getDeltaTime());
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }
}
