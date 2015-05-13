package com.emptypockets.spacemania.network.server.engine;

import com.badlogic.gdx.Gdx;
import com.emptypockets.spacemania.engine.entities.BaseEntity;
import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.entities.FixedTimeEntity;
import com.emptypockets.spacemania.engine.players.Player;
import com.emptypockets.spacemania.engine.players.PlayerList;
import com.emptypockets.spacemania.network.server.engine.playerProcessors.PlayerUpdateProcessor;

/**
 * Created by jenfield on 11/05/2015.
 */
public class ServerGameEngine extends GameEngine {

    PlayerList<ServerPlayer> players;
    PlayerUpdateProcessor playerUpdateProcessor;

    public ServerGameEngine() {
        super();
        players = new PlayerList();
        playerUpdateProcessor = new PlayerUpdateProcessor(this);
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
        synchronized (this) {
            players.processPlayers(playerUpdateProcessor);
        }
        super.update();
    }
}
