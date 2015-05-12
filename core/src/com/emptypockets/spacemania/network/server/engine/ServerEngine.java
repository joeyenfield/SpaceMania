package com.emptypockets.spacemania.network.server.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.engine.BaseEntity;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.entityManager.BulletEntity;
import com.emptypockets.spacemania.network.server.Player;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by jenfield on 11/05/2015.
 */
public class ServerEngine extends GameEngine {

    HashMap<String, Player> players;

    public ServerEngine() {
        super();
        players = new HashMap<String, Player>();
    }

    public Player removePlayer(String name) {

        Player player = players.remove(name);
        removeEntity(player.getPlayerId());
        return player;
    }

    public Player addPlayer(String name) {
        BaseEntity entity = new BaseEntity();
        entity.getPos().set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        Player player = new Player();
        player.setPlayerId(entity.getId());
        player.setPlayerName(name);
        players.put(name, player);
        addEntity(entity);
        return player;
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public Player getPlayerByName(String username) {
        return players.get(username);
    }

    @Override
    public void update() {
        for(Player p : getPlayers()){
            BaseEntity ent = getEntityById(p.getPlayerId());
            ent.getVel().set(p.getMovement()).scl(300);

            if(p.getShoot().len2() > 0.3*0.3f){
                BulletEntity bullet = new BulletEntity();
                bullet.getPos().set(ent.getPos()).add(p.getShoot().cpy().setLength(10));

                bullet.getVel().set(p.getShoot()).setLength(500);
                addEntity(bullet);
            }
        }
        super.update();
    }
}
