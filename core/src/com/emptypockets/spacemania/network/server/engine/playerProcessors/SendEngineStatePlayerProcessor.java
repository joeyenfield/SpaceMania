package com.emptypockets.spacemania.network.server.engine.playerProcessors;

import com.emptypockets.spacemania.engine.players.processor.PlayerProcessor;
import com.emptypockets.spacemania.network.client.payloads.engine.stateSync.EngineStatePayload;
import com.emptypockets.spacemania.network.server.engine.ServerPlayer;

/**
 * Created by jenfield on 12/05/2015.
 */
public class SendEngineStatePlayerProcessor implements PlayerProcessor<ServerPlayer> {
    EngineStatePayload engineState;

    @Override
    public void processPlayer(ServerPlayer player) {
        player.getConnection().sendUDP(engineState);
    }

    public EngineStatePayload getEngineState() {
        return engineState;
    }

    public void setEngineState(EngineStatePayload engineState) {
        this.engineState = engineState;
    }
}
