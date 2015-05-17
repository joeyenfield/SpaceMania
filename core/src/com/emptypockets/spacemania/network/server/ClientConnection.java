package com.emptypockets.spacemania.network.server;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.transport.ComsType;
import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;

public class ClientConnection extends Connection {
    ServerManager serverManager;
    ServerPlayer player;
    private boolean loggedIn;

    ArrayList<ServerPayload> incommingPayloads;

    public ClientConnection(ServerManager serverManager){
        this.serverManager = serverManager;
        incommingPayloads = new ArrayList<ServerPayload>();
    }

    public void send(ClientPayload payload){
        try {
            if(payload.getComsType() == ComsType.TCP) {
                sendTCP(payload);
            }else{
                sendUDP(payload);
            }
        }catch(Throwable t){
            Console.println("Error sending payload");
            Console.error(t);
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
