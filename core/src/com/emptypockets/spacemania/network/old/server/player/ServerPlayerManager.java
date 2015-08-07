package com.emptypockets.spacemania.network.old.server.player;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.holders.ObjectProcessor;
import com.emptypockets.spacemania.network.old.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.old.engine.sync.PlayerManagerSync;
import com.emptypockets.spacemania.network.old.server.exceptions.TooManyPlayersException;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;

/**
 * Created by jenfield on 12/05/2015.
 */
public class ServerPlayerManager extends ObjectProcessor<ServerPlayer> implements Disposable {
    ArrayList<ServerPlayer> players;
    int maxPlayers = 0;

    public ServerPlayerManager() {
        players = new ArrayList<ServerPlayer>();
    }

    public synchronized void addPlayer(ServerPlayer player) throws TooManyPlayersException {
        if(maxPlayers != 0 && getPlayerCount() >= maxPlayers){
            throw new TooManyPlayersException();
        }
        players.add(player);
    }
    
    public synchronized ServerPlayer getPlayerByUsername(String name) {
        for (ServerPlayer p : players) {

            if (p.getUsername() != null && p.getUsername().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public synchronized ServerPlayer getPlayerById(int id) {
        for (ServerPlayer p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public synchronized void removePlayer(ServerPlayer player) {
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
    protected synchronized Iterator<ServerPlayer> getIterator() {
        return players.iterator();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public synchronized void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public synchronized void removePlayerById(int playerId) {
        ServerPlayer player = getPlayerById(playerId);
        removePlayer(player);
    }

    public synchronized int getSize() {
        return players.size();
    }
}
