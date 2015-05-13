package com.emptypockets.spacemania.network.server;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.emptypockets.spacemania.commandLine.CommandLine;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.engine.players.Player;
import com.emptypockets.spacemania.engine.players.PlayerList;
import com.emptypockets.spacemania.engine.players.processor.PlayerProcessor;
import com.emptypockets.spacemania.logging.ServerLogger;
import com.emptypockets.spacemania.network.CommandService;
import com.emptypockets.spacemania.network.NetworkProperties;
import com.emptypockets.spacemania.network.server.engine.ServerGameRoom;
import com.emptypockets.spacemania.network.server.engine.ServerPlayer;
import com.emptypockets.spacemania.network.server.payloads.ServerPayload;
import com.emptypockets.spacemania.network.transport.NetworkProtocall;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerManager extends Listener implements Disposable {

    String name = "Server";
    Server server;

    int connectedUserCount = 0;
    int createdRoomCount = 0;

    CommandLine command;
    int udpPort = NetworkProperties.udpPort;
    int tcpPort = NetworkProperties.tcpPort;

    PlayerList<ServerPlayer> playerList;
    ArrayList<ServerGameRoom> rooms;
    Timer pingingScheduled;

    public ServerManager() {
        setupServer();
        command = new CommandLine();
        CommandService.registerServer(this);
        NetworkProtocall.register(server.getKryo());
        playerList = new PlayerList<ServerPlayer>();
        rooms = new ArrayList<ServerGameRoom>();


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

        //PINGER
        pingingScheduled = new Timer();
        pingingScheduled.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                updatePings();
            }
        }, 20, 20);
        pingingScheduled.start();
    }

    public void updatePings(){
        playerList.processPlayers(new PlayerProcessor<ServerPlayer>() {
            @Override
            public void processPlayer(ServerPlayer player) {
                try {
                    player.getConnection().updateReturnTripTime();
                } catch (Throwable t) {
                    Console.printf("Failed to update return trip time for player " + player.getUsername());
                    Console.error(t);
                }
            }
        });
    }
    public void stop() {
        Console.println("Stopping Server");
        pingingScheduled.stop();
        server.stop();

    }

    public synchronized ServerGameRoom createRoom(ServerPlayer host, String roomName) {
        createdRoomCount++;
        //Set State
        ServerGameRoom room = new ServerGameRoom(this);
        room.setId(createdRoomCount);
        room.setName(roomName);
        rooms.add(room);

        //Join the room
        room.setHost(host);
        return room;
    }

    public ServerGameRoom getRoomById(int roomId) {
        return null;
    }

    public void clientLogout(ClientConnection connection) {
        Console.println("Client Exit : " + name);
        ServerLogger.info(name, "Client Exit : " + name);
        if (connection.isLoggedIn()) {
            playerList.removePlayer(connection.getPlayer());
            connection.getPlayer().setConnection(null);
            connection.setPlayer(null);
            connection.setLoggedIn(false);
        }
    }

    public synchronized ServerPlayer clientLogin(ClientConnection connection, String username, String password) {
        Console.println("Client Join : " + name);
        ServerLogger.info(name, "Client Join : " + name);

        connectedUserCount++;

        ServerPlayer player = new ServerPlayer();
        player.setUsername(username);
        player.setId(connectedUserCount);
        player.setConnection(connection);
        connection.setPlayer(player);
        playerList.addPlayer(player);
        return player;
    }

    public void sendToPlayerUDP(ServerPlayer player, Object data) {
        player.getConnection().sendUDP(data);
    }

    public void sendToPlayerTCP(ServerPlayer player, Object data) {
        player.getConnection().sendTCP(data);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        if (connection instanceof ClientConnection) {
            clientLogout(((ClientConnection) connection));
        }
    }


    public boolean isUserConnected(String userName) {
        ServerPlayer player = playerList.getPlayerByUsername(userName);
        return player != null;
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        if (connection instanceof ClientConnection) {
            ClientConnection clientConnection = (ClientConnection) connection;
            if(clientConnection.getPlayer()!=null){
                clientConnection.getPlayer().setPing(connection.getReturnTripTime());
            }
            if (object instanceof ServerPayload) {
                ((ServerPayload) object).setClientConnection(clientConnection);
                ((ServerPayload) object).setServerManager(this);
                ((ServerPayload) object).executePayload();
            }
        }
    }

    public void logStatus() {
        ServerLogger.info(name, "Server Running - Connected [" + server.getConnections().length + "]");
    }

    public void logUsers() {
        ServerLogger.info(name, "Connected [" + server.getConnections().length + "]" + " Players [" + playerList.getPlayerCount() + "]");
        playerList.processPlayers(new PlayerProcessor<ServerPlayer>() {
            @Override
            public void processPlayer(ServerPlayer player) {
                ServerLogger.info("Player [" + player.getUsername() + "](" + player.getPing() + ")ms - In Room[" + player.isInRoom() + "]");
            }
        });
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


    public void pingUser(String username) {
        ServerPlayer player = playerList.getPlayerByUsername(username);
        if(player != null){
            player.getConnection().updateReturnTripTime();
        }else{
            Console.println("No user "+username);
        }

    }
}