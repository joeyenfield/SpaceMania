package com.emptypockets.spacemania.network.client;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.client.exceptions.ClientNotConnectedException;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.transport.ComsType;
import com.emptypockets.spacemania.network.transport.NetworkPayload;
import com.emptypockets.spacemania.network.transport.NetworkProtocall;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jenfield on 12/05/2015.
 */
public class ClientConnectionManager extends Listener implements Disposable{
    ArrayList<ClientPayload> recievedPayloads;
    ArrayList<ServerPayload> toSendPayloads;

    Object clientConnectionLock = new Object();
    Client connection;
    ClientManager manager;

    public ClientConnectionManager(ClientManager manager) {
        this.manager = manager;
        recievedPayloads = new ArrayList<ClientPayload>();
        toSendPayloads = new ArrayList<ServerPayload>();
    }

    public static void listNetworkServers(final int udpPort, final int timeoutSec, final NetworkDiscoveryInterface callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Client connection = new Client();

                // TODO Auto-generated method stub
                Console.println("Searching for hosts Port [" + udpPort + "] holding for [" + timeoutSec + " s]");
                List<InetAddress> hosts = connection.discoverHosts(udpPort, timeoutSec * 1000);
                Console.println("Found : " + hosts.size());
                for (InetAddress host : hosts) {
                    Console.println("Host : " + host.getHostAddress() + " - " + host.getHostName());
                }
                if (callback != null) {
                    callback.notifyDiscoveredHosts(hosts);
                }
            }
        }).start();
    }

    private void setupClientConnection() {
        synchronized (clientConnectionLock) {
            disconnect();
            connection = new Client(10 * 1024 * 1024, 10 * 1024 * 1024);
            connection.start();
            connection.addListener(this);
            NetworkProtocall.register(connection.getKryo());
        }
    }

    public void connect(String address, int tcpPort, int udpPort) throws IOException {
        Console.printf("Connecting to server %s : %d,%d\n", address, tcpPort, udpPort);
        synchronized (clientConnectionLock) {
            setupClientConnection();
            connection.connect(20000, address, tcpPort, udpPort);
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        if (object instanceof ClientPayload) {
            addPayload((ClientPayload) object);
        }
    }

    public boolean isConnected() {
        synchronized (clientConnectionLock) {
            if (connection != null && connection.isConnected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void idle(Connection connection) {
        super.idle(connection);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        disconnect();
    }

    public void disconnect() {
        Console.println("Disconnecting from server");
        processToSendPayloads();
        synchronized (clientConnectionLock) {
            if (connection != null && connection.isConnected()) {
                connection.close();
                connection.stop();
                connection = null;
            }
        }
        manager.postDisconnect();
    }

    public void listStatus() {
        synchronized (clientConnectionLock) {
            if (connection == null) {
                Console.println("Not Connected");
            } else {
                Console.println("Connected : " + connection.isConnected()+" : Ping ["+getPing()+"]");
            }
        }
    }

    public void send(ServerPayload payload){
        synchronized (toSendPayloads){
            toSendPayloads.add(payload);
        }
    }
    private void sendTCP(Object data) {
        synchronized (clientConnectionLock) {
            //Send Current State to server
            if (connection != null && connection.isConnected()) {
                connection.sendTCP(data);
            } else {
                throw new ClientNotConnectedException();
            }
        }
    }

    private void sendUDP(Object data) {
        synchronized (clientConnectionLock) {
            //Send Current State to server
            if (connection != null && connection.isConnected()) {
                connection.sendUDP(data);
            } else {
                throw new ClientNotConnectedException();
            }
        }
    }

    public void updatePing() {
        synchronized (clientConnectionLock){
            if (connection != null && connection.isConnected()) {
                connection.updateReturnTripTime();
            }
        }
    }

    public int getPing(){
        synchronized (clientConnectionLock){
            if (connection != null && connection.isConnected()) {
                return connection.getReturnTripTime();
            }
        }
        return -1;
    }

    public void addPayload(ClientPayload object) {
        synchronized (recievedPayloads) {
            recievedPayloads.add(object);
        }
    }

    public void processRecievedPayloads(){
        //Process Payloads
        synchronized (recievedPayloads) {
            for (ClientPayload payload : recievedPayloads) {
                payload.executePayload(manager);
            }
            recievedPayloads.clear();
        }
    }

    public void processToSendPayloads(){
        //Send Coms to Server
        synchronized (toSendPayloads){
            for(ServerPayload payload : toSendPayloads){
                try {
                    if (payload.getComsType() == ComsType.UDP) {
                        sendUDP(payload);
                    } else {
                        sendTCP(payload);
                    }
                }catch(Throwable t){
                    Console.println("Failed to send packet");
                    Console.error(t);
                }
            }
            toSendPayloads.clear();
        }
    }

    public void dispose() {
        disconnect();
        synchronized (toSendPayloads) {
            toSendPayloads.clear();
        }
        synchronized (recievedPayloads) {
            recievedPayloads.clear();
        };
    }
}
