package com.emptypockets.spacemania.engine.entityManager.processors;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.entities.BaseEntity;

/**
 * Created by jenfield on 10/05/2015.
 */
public class BoundedEntityProcessor implements EntityProcessor<BaseEntity> {

    Rectangle region = new Rectangle();

    public BoundedEntityProcessor() {
        region.set(0, 0, 0, 0);
    }

    public Rectangle getRegion() {
        return region;
    }

    public void setBounds(float x, float y, float wide, float high) {
        region.set(x, y, wide, high);
    }

    @Override
    public void processEntity(BaseEntity entity) {
        if (entity.getPos().x < region.x) {
            entity.getPos().x = region.x;
            entity.getVel().x *= -1;
        }
        if (entity.getPos().x > region.x + region.width) {
            entity.getPos().x = region.x + region.width;
            entity.getVel().x *= -1;
        }

        if (entity.getPos().y < region.y) {
            entity.getPos().y = region.y;
            entity.getVel().y *= -1;
        }
        if (entity.getPos().y > region.y + region.height) {
            entity.getPos().y = region.y + region.height;
            entity.getVel().y *= -1;
        }
    }
}
