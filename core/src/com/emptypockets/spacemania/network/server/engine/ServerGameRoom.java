package com.emptypockets.spacemania.network.server.engine;

import com.emptypockets.spacemania.engine.players.Player;
import com.emptypockets.spacemania.engine.players.PlayerList;
import com.emptypockets.spacemania.engine.rooms.GameRoom;
import com.emptypockets.spacemania.network.client.payloads.engine.stateSync.EngineStatePayload;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.engine.playerProcessors.SendEngineStatePlayerProcessor;

/**
 * Created by jenfield on 12/05/2015.
 */
public class ServerGameRoom extends GameRoom<ServerPlayer, ServerGameEngine> {
    ServerManager manager;
    ServerGameEngine engine;
    EngineStatePayload engineStatePayload;
    SendEngineStatePlayerProcessor sendEngineStateProcessor;

    private Player host;

    public ServerGameRoom(ServerManager manager) {
        super();
        this.manager = manager;
        engineStatePayload = new EngineStatePayload();
        sendEngineStateProcessor = new SendEngineStatePlayerProcessor();
    }

    @Override
    public PlayerList<ServerPlayer> createPlayerList() {
        return new PlayerList<ServerPlayer>();
    }

    @Override
    public ServerGameEngine createEngine() {
        return new ServerGameEngine();
    }

    public void joinGame(ServerPlayer player) {
        player.setRoom(this);
        addPlayer(player);
    }

    public void leaveGame(ServerPlayer player) {
        player.setRoom(null);
        removePlayer(player);
    }

    public void update() {
        super.update();
        broadcast();
    }

    public void broadcast() {
        synchronized (engine) {
            engineStatePayload.readState(engine);
        }
        sendEngineStateProcessor.setEngineState(engineStatePayload);
        players.processPlayers(sendEngineStateProcessor);
    }

    public Player getHost() {
        return host;
    }

    public void setHost(Player host) {
        this.host = host;
    }
}
