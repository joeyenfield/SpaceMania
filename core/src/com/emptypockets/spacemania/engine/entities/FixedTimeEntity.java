package com.emptypockets.spacemania.engine.entities;

/**
 * Created by jenfield on 12/05/2015.
 */
public class FixedTimeEntity extends BaseEntity {
    long lifeTime = 1000;

    long creationTime = 0;

    public FixedTimeEntity() {
        super();
        updateCreationTime();
    }

    public void updateCreationTime() {
        creationTime = System.currentTimeMillis();
    }

    public boolean isDead() {
        return System.currentTimeMillis() - creationTime > lifeTime;
    }
}
