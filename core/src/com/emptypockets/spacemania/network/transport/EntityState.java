package com.emptypockets.spacemania.network.transport;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.emptypockets.spacemania.engine.BaseEntity;

/**
 * Created by jenfield on 11/05/2015.
 */
public class EntityState {
    int id;
    Vector2 pos = new Vector2();
    Vector2 vel = new Vector2();

    float ang;
    float angVel;

    long time;

    public void read(long time, BaseEntity entity) {
        id = entity.getId();

        pos.set(entity.getPos());
        ang = entity.getAng();

        vel.set(entity.getVel());
        angVel = entity.getAngVel();

        this.time = time;
    }

    public void write(long currentTime, BaseEntity ent, boolean forcePos){
        ent.setVel(vel);
        ent.setAngVel(angVel);

        if(forcePos || Math.abs(ang-ent.getAng()) > 2*angVel){
            ent.setAng(ang);
        }

        if(forcePos || pos.dst2(ent.getPos()) > 2*vel.len2()){
            ent.setPos(pos);
        }


    }

    public int getId() {
        return id;
    }
}
