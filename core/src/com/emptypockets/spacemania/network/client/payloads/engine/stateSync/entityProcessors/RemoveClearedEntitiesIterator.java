package com.emptypockets.spacemania.network.client.payloads.engine.stateSync.entityProcessors;


import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.engine.entityManager.processors.EntityIterator;
import com.emptypockets.spacemania.network.client.payloads.engine.stateSync.EngineStatePayload;
import com.emptypockets.spacemania.network.transport.EntityState;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jenfield on 13/05/2015.
 */
public class RemoveClearedEntitiesIterator implements EntityIterator<BaseEntity> {

    EngineStatePayload engineStatePayload;
    public RemoveClearedEntitiesIterator(EngineStatePayload engineStatePayload){
        this.engineStatePayload = engineStatePayload;
    }

    @Override
    public void iteratateEntities(Iterator<BaseEntity> entities) {
        //Build Entity Map
        HashMap<Integer, EntityState> stateMap = new HashMap<Integer,EntityState>();
        for(EntityState state : engineStatePayload.getStates()){
            stateMap.put(state.getId(), state);
        }

        while(entities.hasNext()){
            BaseEntity entity = entities.next();
            if(!stateMap.containsKey(entity.getId())){
                entities.remove();
            }
        }
    }
}
