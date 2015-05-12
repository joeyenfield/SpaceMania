package com.emptypockets.spacemania.network.server.engine;

import com.badlogic.gdx.Gdx;
import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.entities.BulletEntity;
import com.emptypockets.spacemania.engine.players.Player;
import com.emptypockets.spacemania.engine.players.PlayerList;

/**
 * Created by jenfield on 11/05/2015.
 */
public class ServerGameEngine extends GameEngine {

    PlayerList<ServerPlayer> players;

    public ServerGameEngine() {
        super();
        players = new PlayerList();
    }

    public void removePlayer(ServerPlayer player) {
        players.removePlayer(player);
        removeEntity(player.getId());
    }

    public Player addPlayer(ServerPlayer player) {
        BaseEntity entity = new BaseEntity();
        entity.getPos().set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        players.addPlayer(player);
        addEntity(entity);
        return player;
    }

    public Player getPlayerByName(String username) {
        return players.getPlayerByUsername(username);
    }

    @Override
    public void update() {
        synchronized (players) {
            for (ServerPlayer p : players.getPlayers()) {
                BaseEntity ent = getEntityById(p.getId());
                ent.getVel().set(p.getMovement()).scl(300);
                if (p.getShoot().len2() > 0.3 * 0.3f) {
                    BulletEntity bullet = new BulletEntity();
                    bullet.getPos().set(ent.getPos()).add(p.getShoot().cpy().setLength(10));
                    bullet.getVel().set(p.getShoot()).setLength(500);
                    addEntity(bullet);
                }
            }
        }
        super.update();
    }
}
