package com.emptypockets.spacemania.network.client.payloads.engine;

import com.badlogic.gdx.math.Rectangle;
import com.emptypockets.spacemania.engine.entityManager.BoundedEntityManager;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;

/**
 * Created by jenfield on 11/05/2015.
 */
public class GameJoinPayload extends ClientPayload {
    Rectangle rect = new Rectangle();
    long ellapsedTime = 0;

    @Override
    public void executePayload() {
        ((BoundedEntityManager)clientManager.getEngine().getEntityManager()).getRegion().set(rect);
        clientManager.startEngine();
        clientManager.getEngine().setTime(ellapsedTime);
    }

    public void read(ServerEngine engine) {
        rect.set(((BoundedEntityManager) engine.getEntityManager()).getRegion());
        ellapsedTime = engine.getTime();
    }
}
