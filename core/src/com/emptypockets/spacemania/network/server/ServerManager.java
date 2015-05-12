package com.emptypockets.spacemania.network.server;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.command.CommandLine;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.logging.ServerLogger;
import com.emptypockets.spacemania.network.CommandService;
import com.emptypockets.spacemania.network.NetworkProperties;
import com.emptypockets.spacemania.network.client.payloads.engine.EngineStatePayload;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.transport.NetworkProtocall;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class ServerManager extends Listener implements Runnable, Disposable {
    public static final int DEFAULT_SERVER_UPDATE = 10;
    String name = "Server";
    Server server;

    long lastBroadcast = 0;

    Thread thread;
    int maxUpdateCount = 0;
    boolean alive = false;
    CommandLine command;
    int udpPort = NetworkProperties.udpPort;
    int tcpPort = NetworkProperties.tcpPort;

    ServerEngine engine;

    public ServerManager() {
        this(DEFAULT_SERVER_UPDATE);
    }

    public ServerManager(int maxUpdateCount) {
        setMaxUpdateCount(maxUpdateCount);
        setupServer();
        engine = new ServerEngine();
        command = new CommandLine();
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
    }

    public void startGame(){
        thread = new Thread(this);
        alive = true;
        thread.start();
    }

    public void stopGame(){
        alive = false;
        thread = null;
    }
    public void stop() {
        Console.println("Stopping Server");
        stopGame();
        server.stop();
    }

    public void clientExit(String name) {
        Console.println("Client Exit : " + name);
        ServerLogger.info(name, "Client Exit : " + name);
        synchronized (engine) {
            engine.removePlayer(name);
        }
    }

    public int clientJoin(String name) {
        Console.println("Client Join : " + name);
        ServerLogger.info(name, "Client Join : " + name);
        synchronized (engine) {
            Player player = engine.addPlayer(name);
            return player.getPlayerId();
        }
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
            engine.update();
        }
    }

    public void broadcast() {
        synchronized (engine) {
            EngineStatePayload state = new EngineStatePayload();
            state.readState(engine);
            Connection[] con = server.getConnections();
            for (int i = 0; i < con.length; i++) {
                if (con[i] instanceof ClientConnection) {
                    ClientConnection c = (ClientConnection) con[i];
                    if (c.isLoggedIn()) {
                        if(engine.getPlayerByName(c.getUsername()) != null) {
                            c.sendTCP(state);
                        }
                    }
                }
            }
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
        if (connection instanceof ClientConnection) {
            ClientConnection clientConnection = (ClientConnection) connection;
            if (object instanceof ServerPayload) {
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
        engine.setStart();
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

    public void logUsers() {
        synchronized (engine) {
            ServerLogger.info(name, "Connected [" + server.getConnections().length + "]" + " Playsers [" + engine.getPlayers().size() + "]");
            for (Player p : engine.getPlayers()) {
                ServerLogger.info(name, "Player [" + p.getPlayerName() + "] - [" + p.getPlayerId() + "]");
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

    public ServerEngine getEngine() {
        return engine;
    }
}