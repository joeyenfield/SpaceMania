package com.emptypockets.spacemania.network.server;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.command.CommandLine;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.engine.ServerEngine;
import com.emptypockets.spacemania.logging.ServerLogger;
import com.emptypockets.spacemania.network.CommandService;
import com.emptypockets.spacemania.network.NetworkProperties;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.transport.NetworkProtocall;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;

public class ServerManager extends Listener implements Runnable, Disposable {
    public static final int DEFAULT_SERVER_UPDATE = 10;
    int clientCount = 0;
    String name = "Server";
    Server server;
    long lastEngineUpdate = 0;
    long lastBroadcast = 0;

    Thread thread;
    int maxUpdateCount = 0;
    boolean alive = false;
    CommandLine command;
    int udpPort = NetworkProperties.udpPort;
    int tcpPort = NetworkProperties.tcpPort;

    ServerEngine engine;

    HashMap<String, Player> players;

    public ServerManager() {
        this(DEFAULT_SERVER_UPDATE);
    }

    public ServerManager(int maxUpdateCount) {
        setMaxUpdateCount(maxUpdateCount);
        setupServer();

        engine = new ServerEngine();
        command = new CommandLine();
        players = new HashMap<String, Player>();
        CommandService.registerServer(this);
        NetworkProtocall.register(server.getKryo());
    }

    public void setupServer() {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                Console.println("Connection Recieved");
                return new ClientConnection();
            }
        };
        server.start();
        server.addListener(this);
    }

    public void start() throws IOException {
        Console.printf("Starting Server [%d,%d]\n", tcpPort, udpPort);
        server.bind(tcpPort, udpPort);
        thread = new Thread(this);
        alive = true;
        thread.start();

    }

    public void stop() {
        Console.println("Stopping Server");
        server.stop();
        alive = false;
        thread = null;
    }

    public void clientExit(String name) {
        Console.println("Client Exit : " + name);
        ServerLogger.info(name, "Client Exit : " + name);
        synchronized (engine) {
            engine.removePlayer(name);
            players.remove(name);
        }
    }

    public int clientJoin(String name) {
        Console.println("Client Join : " + name);
        ServerLogger.info(name, "Client Join : " + name);
        int id;
        synchronized (engine) {
            id = clientCount++;
            Player player = new Player();
            player.setPlayerId(id);
            player.setPlayerName(name);
            players.put(name, player);
        }
        return id;
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        if (connection instanceof ClientConnection) {
            clientExit(((ClientConnection) connection).getUsername());
        }
    }

    public void update() {
        synchronized (engine) {
            if (lastEngineUpdate == 0) {
                lastEngineUpdate = System.currentTimeMillis();
                return;
            }
            float time = (System.currentTimeMillis() - lastEngineUpdate) * 1e-3f;
            engine.update(time);
            engine.tick();
            lastEngineUpdate = System.currentTimeMillis();
        }
    }

    public void broadcast() {
        synchronized (engine) {
            Connection[] con = server.getConnections();
//            for (int i = 0; i < con.length; i++) {
//                if (con[i] instanceof ClientConnection) {
//                    ClientConnection c = (ClientConnection) con[i];
//                    if (c.username != null) {
//                        c.sendUDP(engine);
//                    }
//                }
//            }
//			server.sendToAllTCP(engine);
        }
    }

    public ClientConnection isUserConnected(String userName) {
        Connection[] con = server.getConnections();
        for (int i = 0; i < con.length; i++) {
            if (con[i] instanceof ClientConnection) {
                ClientConnection c = (ClientConnection) con[i];
                if (c.getUsername() != null && userName != null && c.getUsername().equalsIgnoreCase(userName)) {
                    return c;
                }
            }
        }
        return null;
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        if(connection instanceof ClientConnection) {
            ClientConnection clientConnection = (ClientConnection)connection;
            if(object instanceof ServerPayload){
                ((ServerPayload) object).setClientConnection(clientConnection);
                ((ServerPayload) object).setServerManager(this);
                ((ServerPayload) object).executePayload();
            }
        }
    }

    @Override
    public void run() {
        long diff;
        float desired;
        Console.println("Starting started");
        while (alive) {
            lastBroadcast = System.currentTimeMillis();
            update();
            broadcast();
            diff = System.currentTimeMillis() - lastBroadcast;
            desired = 1000f / maxUpdateCount;
            if (diff < desired) {
                try {
                    Thread.sleep((long) (desired - diff));
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void logStatus() {
        ServerLogger.info(name, "Server Running [" + alive + "] Connected [" + server.getConnections().length + "]");
    }

    public void logUsers(){
        synchronized (engine) {
            ServerLogger.info(name, "Connected [" + server.getConnections().length + "]" + " Playsers [" + players.size() + "]");
            for(Player p : players.values()){
                ServerLogger.info(name, "Player ["+p.getPlayerName()+"] - ["+p.getPlayerId()+"]");
            }
        }
    }
    public void setMaxUpdateCount(int maxUpdateCount) {
        this.maxUpdateCount = maxUpdateCount;
    }

    public CommandLine getCommand() {
        return command;
    }

    public void setCommand(CommandLine command) {
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void dispose() {
        stop();
    }
}