package com.emptypockets.spacemania.engine.players;

import com.emptypockets.spacemania.engine.players.processor.PlayerIterator;
import com.emptypockets.spacemania.engine.players.processor.PlayerProcessor;

import java.util.ArrayList;

/**
 * Created by jenfield on 12/05/2015.
 */
public class PlayerList<PLR extends Player> {
    ArrayList<PLR> players;

    public PlayerList() {
        players = new ArrayList<PLR>();
    }

    public synchronized void addPlayer(PLR player) {
        players.add(player);
    }

    public synchronized PLR getPlayerByUsername(String name) {
        for (PLR p : players) {
            if (p.getUsername().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public synchronized void removePlayer(PLR player) {
        players.remove(player);
    }

    public synchronized int getPlayerCount() {
        return players.size();
    }

    public synchronized void processPlayers(PlayerProcessor<PLR> processor){
        for(PLR player : players){
            processor.processPlayer(player);
        }
    }

    public synchronized void iteratoratePlayers(PlayerIterator<PLR> iterator){
        iterator.iterateOver(players.iterator());
    }
}
