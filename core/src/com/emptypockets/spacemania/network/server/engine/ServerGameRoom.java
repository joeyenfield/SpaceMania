package com.emptypockets.spacemania.network.server.engine;

import com.emptypockets.spacemania.network.client.payloads.engine.EngineStatePayload;
import com.emptypockets.spacemania.engine.players.Player;
import com.emptypockets.spacemania.network.server.ServerManager;

import java.util.ArrayList;

/**
 * Created by jenfield on 12/05/2015.
 */
public class ServerGameRoom implements Runnable {
    ServerManager manager;
    String name;
    ArrayList<Player> players;
    ServerGameEngine engine;

    public ServerGameRoom(ServerManager manager){
        this.manager = manager;
    }
    public void joinGame(Player player){

    }

    public void leaveGame(Player player){

    }

    public void start(){
        engine.start();
    }

    public void update() {
        synchronized (engine) {
            engine.update();
        }
    }

    public void broadcast() {
        EngineStatePayload state = new EngineStatePayload();
        synchronized (engine) {
            state.readState(engine);
        }

        synchronized (players) {
            for(Player player : players) {
                manager.sendToPlayerTCP((ServerPlayer) player, state);
            }
        }
    }


    @Override
    public void run() {

    }
}
