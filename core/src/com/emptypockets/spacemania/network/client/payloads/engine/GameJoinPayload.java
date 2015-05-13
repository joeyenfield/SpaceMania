package com.emptypockets.spacemania.network.client.payloads.engine;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.server.engine.ServerGameEngine;

/**
 * Created by jenfield on 11/05/2015.
 */
public class GameJoinPayload extends ClientPayload {
    Rectangle rect = new Rectangle();
    long ellapsedTime = 0;

    @Override
    public void executePayload() {
        clientManager.getEngine().getBoundedRegion().set(rect);
        clientManager.startEngine();
        clientManager.getEngine().setTime(ellapsedTime);
    }

    public void read(ServerGameEngine engine) {
        rect.set(engine.getBoundedRegion());
        ellapsedTime = engine.getTime();
    }
}
