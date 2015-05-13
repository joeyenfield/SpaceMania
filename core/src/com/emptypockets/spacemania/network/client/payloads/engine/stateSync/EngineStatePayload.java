package com.emptypockets.spacemania.network.client.payloads.engine.stateSync;

import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.network.client.engine.ClientEngine;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.client.payloads.engine.stateSync.entityProcessors.ReadEntityStateEntityProcessor;
import com.emptypockets.spacemania.network.client.payloads.engine.stateSync.entityProcessors.RemoveClearedEntitiesIterator;
import com.emptypockets.spacemania.network.server.engine.ServerGameEngine;
import com.emptypockets.spacemania.network.transport.EntityState;

import java.util.ArrayList;

/**
 * Created by jenfield on 11/05/2015.
 */
public class EngineStatePayload extends ClientPayload {
    long engineTime;
    ArrayList<EntityState> states = new ArrayList<EntityState>();

    ReadEntityStateEntityProcessor readStateProcessor = new ReadEntityStateEntityProcessor(this);
    RemoveClearedEntitiesIterator removeClearedEntitiesProcessore = new RemoveClearedEntitiesIterator(this);

    public void readState(ServerGameEngine engine) {
        states.clear();
        engineTime = (engine.getTime());
        engine.getEntityManager().processEntities(readStateProcessor);
    }


    @Override
    public void executePayload() {
        ClientEngine engine = clientManager.getEngine();
        synchronized (engine) {
            //Remove Old Entities
            engine.getEntityManager().processEntities(removeClearedEntitiesProcessore);

            //Write Entity states and Add new Entities
            for (EntityState state : states) {
                BaseEntity entity = engine.getEntityById(state.getId());
                boolean forcePos = false;
                if (entity == null) {
                    entity = new BaseEntity();
                    entity.setId(state.getId());
                    engine.addEntity(entity);
                    forcePos = true;
                }
                state.write(engine.getTime(), entity, forcePos);
            }
        }
    }

    public long getEngineTime() {
        return engineTime;
    }

    public void setEngineTime(long engineTime) {
        this.engineTime = engineTime;
    }

    public ArrayList<EntityState> getStates() {
        return states;
    }

    public void setStates(ArrayList<EntityState> states) {
        this.states = states;
    }
}
