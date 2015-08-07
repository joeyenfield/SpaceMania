package com.emptypockets.spacemania.network.old.client.player;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.holders.ObjectProcessor;
import com.emptypockets.spacemania.network.old.server.exceptions.TooManyPlayersException;

/**
 * Created by jenfield on 12/05/2015.
 */
public class ClientPlayerManager extends ObjectProcessor<ClientPlayer> implements Disposable {
    ArrayList<ClientPlayer> players;
    int maxPlayers = 0;

    public ClientPlayerManager() {
        players = new ArrayList<ClientPlayer>();
    }

    public synchronized void addPlayer(ClientPlayer player){
        players.add(player);
    }

    public synchronized ClientPlayer getPlayerByUsername(String name) {
        for (ClientPlayer p : players) {
            if (p.getUsername() != null && p.getUsername().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public synchronized ClientPlayer getPlayerById(int id) {
        for (ClientPlayer p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public synchronized void removePlayer(ClientPlayer player) {
        players.remove(player);
    }

    public synchronized int getPlayerCount() {
        return players.size();
    }

    public synchronized void clear() {
        players.clear();
    }

    public void dispose() {
        clear();
    }

    public synchronized boolean isEmpty() {
        return players.isEmpty();
    }

    @Override
    protected synchronized Iterator<ClientPlayer> getIterator() {
        return players.iterator();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public synchronized void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public synchronized void removePlayerById(int playerId) {
        ClientPlayer player = getPlayerById(playerId);
        removePlayer(player);
    }

    public synchronized int getSize() {
        return players.size();
    }
}
