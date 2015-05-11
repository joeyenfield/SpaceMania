package com.emptypockets.spacemania.network.transport;

import com.badlogic.gdx.utils.Pool;

/**
 * Created by jenfield on 11/05/2015.
 */
public abstract class NetworkPayload implements Pool.Poolable{

    public abstract void executePayload();

    @Override
    public void reset() {
    }
}
