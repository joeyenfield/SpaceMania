package com.emptypockets.spacemania.engine.players;

import java.util.ArrayList;

/**
 * Created by jenfield on 12/05/2015.
 */
public class PlayerList<PLR extends Player> {
    ArrayList<PLR> players;

    public PlayerList(){
        players = new ArrayList<PLR>();
    }
    public void addPlayer(PLR player){
        synchronized (players){
            players.add(player);
        }
    }

    public PLR getPlayerByUsername(String name){
        synchronized (players) {
            for (PLR p : players) {
                if(p.getUsername().equalsIgnoreCase(name)){
                    return p;
                }
            }
        }
        return null;
    }

    public void removePlayer(PLR player){
        synchronized (players){
            players.remove(player);
        }
    }

    public int getPlayerCount() {
        synchronized (players){
            return players.size();
        }
    }

    public ArrayList<PLR> getPlayers() {
        return players;
    }
}
