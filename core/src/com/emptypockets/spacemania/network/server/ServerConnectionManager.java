package com.emptypockets.spacemania.network.server;

import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.NetworkProperties;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.transport.NetworkProtocall;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jenfield on 14/05/2015.
 */
public class ServerConnectionManager extends Listener {
    ServerManager manager;

    ArrayList<ServerPayload> incommingPayloads;

    Server server;
    int udpPort = NetworkProperties.udpPort;
    int tcpPort = NetworkProperties.tcpPort;

    public ServerConnectionManager(ServerManager manager){
        this.manager = manager;
        incommingPayloads = new ArrayList<ServerPayload>();
        setupServer();
        NetworkProtocall.register(server.getKryo());
    }

    protected synchronized ClientConnection createConnection(){
        return new ClientConnection(manager);
    }

    public void setupServer() {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                return createConnection();
            }
        };
        server.start();
        server.addListener(this);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        if (connection instanceof ClientConnection) {
            manager.clientLogout(((ClientConnection) connection));
        }
    }

    public void start() throws IOException {
        Console.printf("Starting Server [%d,%d]\n", tcpPort, udpPort);
        server.bind(tcpPort, udpPort);
    }

    public void stop(){
        server.stop();
    }


    @Override
    public synchronized void received(Connection connection, Object object) {
        super.received(connection, object);
        if (connection instanceof ClientConnection) {
            ClientConnection clientConnection = (ClientConnection) connection;
            if(clientConnection.getPlayer()!=null){
                clientConnection.getPlayer().setPing(connection.getReturnTripTime());
            }
            if (object instanceof ServerPayload) {
                ((ClientConnection) connection).recieve((ServerPayload) object);
            }
        }
    }



    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public int getConnectedCount() {
        return server.getConnections().length;
    }

    public synchronized void processIncommingPackets() {
        for(Connection connection : server.getConnections()){
            if (connection instanceof ClientConnection) {
                ClientConnection clientConnection = (ClientConnection) connection;
                clientConnection.processIncommingPayloads();
            }
        }
    }
}
