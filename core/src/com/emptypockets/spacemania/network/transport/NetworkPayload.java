package com.emptypockets.spacemania.network.transport;

import com.badlogic.gdx.utils.Pool;

/**
 * Created by jenfield on 11/05/2015.
 */
public abstract class NetworkPayload implements Pool.Poolable {

//    ComsType comsType = ComsType.TCP;
//
//    public ComsType getComsType() {
//        return comsType;
//    }
//
//    public void setComsType(ComsType comsType) {
//        this.comsType = comsType;
//    }

    @Override
    public void reset() {
    }
}
