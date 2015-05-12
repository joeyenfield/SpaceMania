package com.emptypockets.spacemania.network.client.payloads.engine;

import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.network.client.engine.ClientEngine;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.server.engine.ServerGameEngine;
import com.emptypockets.spacemania.network.transport.EntityState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jenfield on 11/05/2015.
 */
public class EngineStatePayload extends ClientPayload {

    ArrayList<EntityState> states = new ArrayList<EntityState>();

    public void readState(ServerGameEngine engine){
        synchronized (engine){
            for(BaseEntity entity : engine.getEntities()){
                EntityState state = new EntityState();
                state.read(engine.getTime(),entity);
                states.add(state);
            }
        }
    }

    @Override
    public void executePayload() {
        ClientEngine engine = clientManager.getEngine();
        synchronized(engine){
            HashMap<Integer, EntityState> stateMap = new HashMap<Integer, EntityState>();

            //Update and add
            for(EntityState state : states){
                stateMap.put(state.getId(), state);
                BaseEntity entity =  engine.getEntityById(state.getId());
                boolean forcePos = false;
                if(entity == null){
                    entity = new BaseEntity();
                    entity.setId(state.getId());
                    engine.addEntity(entity);
                    forcePos = true;
                }
                state.write(engine.getTime(), entity, forcePos);
            }

            //Remove Old Clients
            Iterator<BaseEntity> entIterator = engine.getEntities().iterator();
            while(entIterator.hasNext()){
                BaseEntity ent = entIterator.next();
                if(!stateMap.containsKey(ent.getId())){
                    entIterator.remove();
                }
            }
        }
    }
}
