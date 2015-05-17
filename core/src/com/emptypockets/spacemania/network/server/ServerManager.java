package com.emptypockets.spacemania.network.server;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.commandLine.CommandLine;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.logging.ServerLogger;
import com.emptypockets.spacemania.network.CommandService;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.server.exceptions.TooManyPlayersException;
import com.emptypockets.spacemania.network.server.player.PlayerManager;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.server.rooms.ServerRoom;
import com.emptypockets.spacemania.network.server.rooms.ServerRoomManager;

import java.io.IOException;
import java.util.ArrayList;

public class ServerManager implements Disposable, Runnable {
    boolean alive = true;
    String name = "Server";
    int connectedUserCount = 0;
    CommandLine command;

    ServerRoom lobbyRoom;
    ServerConnectionManager connectionManager;

    PlayerManager playerManager;
    ServerRoomManager roomManager;


    Thread thread;

    long pingUpdateTime = 30000;
    long lastPingUpdate = 0;


    public ServerManager() {
        command = new CommandLine();
        CommandService.registerServerCommands(this);

        playerManager = new PlayerManager();
        connectionManager = new ServerConnectionManager(this);
        roomManager = new ServerRoomManager(this);
        lobbyRoom = roomManager.createNewRoom(null, "Lobby");
    }

    public ServerRoomManager getRoomManager() {
        return roomManager;
    }

    public void start() throws IOException {
        //Startup the server
        connectionManager.start();
        alive = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        Console.println("Stopping Server");

        //Stop Server
        connectionManager.stop();
        alive = false;
    }

    public ServerRoom createRoom(ServerPlayer host, String roomName) {
        return roomManager.createNewRoom(host, roomName);
    }

    public void clientLogout(ClientConnection connection) {
        Console.println("Client Exit : " + name);
        ServerLogger.info(name, "Client Exit : " + name);
        if (connection.isLoggedIn()) {
            ServerPlayer player = connection.getPlayer();

            connection.setPlayer(null);
            connection.setLoggedIn(false);

            playerManager.removePlayer(player);
            if (player.getCurrentRoom() != null) {
                player.getCurrentRoom().leaveRoom(player);
            }
        }
    }

    public boolean authenticateLogin(String username, String password) {
        if (isUserConnected(username)) {
            return false;
        }
        return true;
    }

    public synchronized ServerPlayer clientLogin(ClientConnection connection, String username, String password) throws TooManyPlayersException {
        Console.println("Client Join : " + name);
        ServerLogger.info(name, "Client Join : " + name);

        connectedUserCount++;

        ServerPlayer player = new ServerPlayer(connection);
        player.setId(connectedUserCount);
        player.setUsername(username);

        playerManager.addPlayer(player);
        connection.setPlayer(player);
        return player;
    }

    public boolean isUserConnected(String userName) {
        ServerPlayer player = playerManager.getPlayerByUsername(userName);
        return player != null;
    }

    public void logStatus() {
        ServerLogger.info(name, "Server Running - Connected [" + connectionManager.getConnectedCount() + "]");
    }

    public void logUsers() {
        ServerLogger.info(name, "Connected [" + connectionManager.getConnectedCount() + "]" + " Players [" + playerManager.getPlayerCount() + "]");
        playerManager.process(new SingleProcessor<ServerPlayer>() {
            @Override
            public void process(ServerPlayer player) {
                ServerLogger.info("Player [" + player.getUsername() + "](" + player.getPing() + ")ms - In Room[" + player.isInRoom() + "]");
            }
        });
    }

    public CommandLine getCommand() {
        return command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void dispose() {
        stop();
        playerManager.dispose();
    }

    public void pingUser(String username) {
        ServerPlayer player = playerManager.getPlayerByUsername(username);
        if (player != null) {
            player.updateReturnTripTime();
        } else {
            Console.println("No user " + username);
        }
    }

    public void updatePings() {
        playerManager.process(new SingleProcessor<ServerPlayer>() {
            @Override
            public void process(ServerPlayer player) {
                try {
                    player.updateReturnTripTime();
                } catch (Throwable t) {
                    Console.printf("Failed to update return trip time for player " + player.getUsername());
                    Console.error(t);
                }
            }
        });
    }

    public ServerRoom getLobbyRoom() {
        return lobbyRoom;
    }

    public void setPorts(int tcpPort, int udpPort) {
        connectionManager.setTcpPort(tcpPort);
        connectionManager.setUdpPort(udpPort);
    }

    @Override
    public void run() {
        while (alive) {

            //Update All Pings
            long delta = System.currentTimeMillis() - lastPingUpdate;
            if (delta > pingUpdateTime) {
                lastPingUpdate = System.currentTimeMillis();
                updatePings();
            }

            //Read All Data
            connectionManager.processIncommingPackets();

            //Update Rooms and client information
            roomManager.process(new SingleProcessor<ServerRoom>() {
                @Override
                public void process(ServerRoom entity) {
                    entity.update();
                    entity.updateClientRoom();
                    entity.broadcast();
                }
            });

            //Update Server Player Client Information
            playerManager.process(new SingleProcessor<ServerPlayer>() {
                @Override
                public void process(ServerPlayer entity) {
                    entity.updateClientPlayer();
                }
            });


            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void closeRoom(ServerRoom room){
        if(room != null && room.equals(getLobbyRoom())){
            return;
        }
        Console.println("Closing room : "+room.getName());
        final ArrayList<ServerPlayer> players = new ArrayList<ServerPlayer>();
        room.getPlayerManager().process(new SingleProcessor<ServerPlayer>() {
            @Override
            public void process(ServerPlayer entity) {
                NotifyClientPayload payload = Pools.obtain(NotifyClientPayload.class);
                payload.setMessage("Room is closed - Returning to lobby.");
                entity.send(payload);

                try {
                    joinRoom(getLobbyRoom(), entity);
                } catch (TooManyPlayersException e) {
                    payload = Pools.obtain(NotifyClientPayload.class);
                    payload.setMessage("Lobby is full your disconnecting.");
                    entity.send(payload);
                }
            }
        });
        roomManager.removeRoom(room);
    }

    public void joinRoom(ServerRoom room, ServerPlayer player) throws TooManyPlayersException {
        if (player.getCurrentRoom() != null) {
            ServerRoom currentRoom =  player.getCurrentRoom();
            currentRoom.leaveRoom(player);
            boolean closeRoom = false;
            if(currentRoom.getHost() != null && currentRoom.getHost().equals(player)){
                closeRoom = true;
            }

            if(currentRoom.getPlayerCount() == 0 && !getLobbyRoom().equals(room)){
                closeRoom = true;
            }

            if(closeRoom) {
                closeRoom(currentRoom);
            }
        }
        room.joinRoom(player);
        player.setCurrentRoom(room);
    }

    public void chatRecieved(ServerPlayer player, String message) {
        player.getCurrentRoom().sendMessage(message, player.getUsername());
    }
}