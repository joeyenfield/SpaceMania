package com.emptypockets.spacemania.network.server.engine.playerProcessors;

import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.engine.entities.FixedTimeEntity;
import com.emptypockets.spacemania.engine.players.processor.PlayerProcessor;
import com.emptypockets.spacemania.network.server.engine.ServerGameEngine;
import com.emptypockets.spacemania.network.server.engine.ServerPlayer;

/**
 * Created by jenfield on 13/05/2015.
 */
public class PlayerUpdateProcessor implements PlayerProcessor<ServerPlayer> {

    ServerGameEngine engine;

    public PlayerUpdateProcessor(ServerGameEngine engine){
        this.engine = engine;
    }
    @Override
    public void processPlayer(ServerPlayer player) {

        BaseEntity ent = engine.getEntityById(player.getId());
        ent.getVel().set(player.getMovement()).scl(300);

        if(player.canShoot()) {
            if (player.getShoot().len2() > 0.3 * 0.3f) {
                FixedTimeEntity bullet = new FixedTimeEntity();
                bullet.getPos().set(ent.getPos()).add(player.getShoot().cpy().setLength(10));
                bullet.getVel().set(player.getShoot()).setLength(500);
                engine.addEntity(bullet);
            }
        }
    }
}
