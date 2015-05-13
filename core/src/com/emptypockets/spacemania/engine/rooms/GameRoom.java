package com.emptypockets.spacemania.engine.rooms;

import com.emptypockets.spacemania.engine.GameEngine;
import com.emptypockets.spacemania.engine.players.Player;
import com.emptypockets.spacemania.engine.players.PlayerList;

/**
 * Created by jenfield on 12/05/2015.
 */
public abstract class GameRoom<PLR extends Player, ENG extends GameEngine> {
    protected PlayerList<PLR> players;
    protected ENG engine;
    String name;
    private int id;

    public GameRoom() {
        players = createPlayerList();
        engine = createEngine();
    }

    public abstract ENG createEngine();

    public abstract PlayerList<PLR> createPlayerList();

    protected void addPlayer(PLR player) {
        players.addPlayer(player);
    }

    protected void removePlayer(PLR player) {
        players.removePlayer(player);
    }

    public void startGame() {
        engine.start();
    }

    public void pauseGame() {
        engine.pause();
    }

    public void unpauseGame() {
        engine.unpause();
    }


    public void update() {
        synchronized (engine) {
            engine.update();
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
