package com.emptypockets.spacemania.network.client.payloads.engine.stateSync.entityProcessors;

import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.engine.entityManager.processors.EntityProcessor;
import com.emptypockets.spacemania.network.client.payloads.engine.stateSync.EngineStatePayload;
import com.emptypockets.spacemania.network.transport.EntityState;

import java.util.ArrayList;

/**
 * Created by jenfield on 12/05/2015.
 */
public class ReadEntityStateEntityProcessor implements EntityProcessor<BaseEntity> {

    EngineStatePayload engineStatePayload;
    public ReadEntityStateEntityProcessor(EngineStatePayload engineStatePayload){
        this.engineStatePayload = engineStatePayload;
    }

    @Override
    public void processEntity(BaseEntity entity) {
        EntityState state = new EntityState();
        state.readStateFromEntity(engineStatePayload.getEngineTime(), entity);
        engineStatePayload.getStates().add(state);
    }
}
