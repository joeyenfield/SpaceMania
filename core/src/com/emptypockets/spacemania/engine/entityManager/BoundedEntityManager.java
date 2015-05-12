package com.emptypockets.spacemania.engine.entityManager;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.entities.BaseEntity;

/**
 * Created by jenfield on 10/05/2015.
 */
public class BoundedEntityManager<ENT extends BaseEntity> extends BaseEntityManager<ENT> {

    Rectangle region = new Rectangle();

    public BoundedEntityManager(){
        region.set(0,0,0,0);
    }

    public Rectangle getRegion() {
        return region;
    }

    public void setBounds(float x, float y, float wide, float high){
        region.set(x,y,wide,high);
    }
    @Override
    protected void updateEntity(ENT ent, float timeDelta) {
        super.updateEntity(ent, timeDelta);

        if (ent.getPos().x < region.x) {
            ent.getPos().x = region.x;
            ent.getVel().x *= -1;
        }
        if (ent.getPos().x > region.x+region.width) {
            ent.getPos().x = region.x+region.width;
            ent.getVel().x *= -1;
        }

        if (ent.getPos().y < region.y) {
            ent.getPos().y = region.y;
            ent.getVel().y *= -1;
        }
        if (ent.getPos().y > region.y+region.height) {
            ent.getPos().y = region.y+region.height;
            ent.getVel().y *= -1;
        }
    }
}
