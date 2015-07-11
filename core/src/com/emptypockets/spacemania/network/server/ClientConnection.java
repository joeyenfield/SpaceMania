package com.emptypockets.spacemania.network.server;

import java.util.ArrayList;

import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.transport.ComsType;
import com.esotericsoftware.kryonet.Connection;

public class ClientConnection extends Connection {
    ServerManager serverManager;
    ServerPlayer player;
    private boolean loggedIn;

    ArrayList<ServerPayload> incommingPayloads;

    public ClientConnection(ServerManager serverManager){
    	super();
        this.serverManager = serverManager;
        incommingPayloads = new ArrayList<ServerPayload>();
    }

    public void send(ClientPayload payload, ComsType type){
        try {
            if(type == ComsType.TCP) {
                sendTCP(payload);
            }else{
                sendUDP(payload);
            }
        }catch(Throwable t){
            serverManager.console.println("Error sending payload");
            serverManager.console.error(t);
        }
    }

    public void recieve(ServerPayload payload){
        synchronized (incommingPayloads){
            incommingPayloads.add(payload);
        }
    }

    public void processIncommingPayloads(){
        synchronized (incommingPayloads){
            for(ServerPayload payload : incommingPayloads){
                payload.executePayload(this, serverManager);
            }
            incommingPayloads.clear();
        }
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ServerPlayer player) {
        this.player = player;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
