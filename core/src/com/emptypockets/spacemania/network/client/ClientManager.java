package com.emptypockets.spacemania.network.client;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.commandLine.CommandLine;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.CommandService;
import com.emptypockets.spacemania.network.client.engine.ClientEngine;
import com.emptypockets.spacemania.network.client.engine.ClientPlayer;
import com.emptypockets.spacemania.network.client.exceptions.ClientNotConnectedException;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.payloads.authentication.LoginRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.authentication.LogoutRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.engine.PlayerStatePayload;

import java.io.IOException;
import java.util.ArrayList;


public class ClientManager implements Disposable {
    ArrayList<ClientPayload> payloads;

    CommandLine command;

    ServerManager serverManager;

    boolean loggedIn = false;

    ClientEngine engine;
    ClientPlayer player;

    ClientConnectionManager connection;

    public ClientManager() {
        setCommand(new CommandLine());
        connection = new ClientConnectionManager(this);
        CommandService.registerClient(this);
        engine = new ClientEngine();
        payloads = new ArrayList<ClientPayload>();
    }


    public void stop() {
        Console.println("Disconnecting from server");
        engine.stop();
        connection.disconnect();
    }

    public void startEngine() {
        engine.start();
    }

    public void pauseEngine() {
        engine.pause();
    }


    public void update() {
        //Process Payloads
        synchronized (payloads) {
            for (ClientPayload payload : payloads) {
                payload.executePayload();
            }
            payloads.clear();
        }

        //Update Server
        engine.update();

        //Send Current State to server
        if (player != null && connection.isConnected()) {
            //Send Player State to Server
            PlayerStatePayload playerState = new PlayerStatePayload();
            playerState.readPlayer(player);

            try {
                connection.sendUDP(playerState);
            } catch (ClientNotConnectedException e) {
                Console.println("Not Connected");
            }
        }

    }


    public void serverLogin(String username, String password) {
        Console.println("Sending Login Request to server");
        LoginRequestPayload request = new LoginRequestPayload();
        request.setUsername(username);
        request.setPassword(password);
        try {
            connection.sendTCP(request);
        } catch (ClientNotConnectedException e) {
            Console.println("Not connected");
        }
    }

    public void serverLogout() {
        Console.println("Sending Logout Request to server");
        LogoutRequestPayload request = new LogoutRequestPayload();
        try {
            connection.sendTCP(request);
        } catch (ClientNotConnectedException e) {
            Console.println("Not connected");
        }
    }

    public void dispose() {
        stop();
        connection.disconnect();
        if (serverManager != null) {
            serverManager.stop();
            serverManager.dispose();
        }
        serverManager = null;

        engine.dispose();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public CommandLine getCommand() {
        return command;
    }

    public void setCommand(CommandLine command) {
        this.command = command;
    }

    public void setupServer() {
        serverManager = new ServerManager();
    }

    public ServerManager getServerManager() {
        if (serverManager == null) {
            serverManager = new ServerManager();
        }
        return serverManager;
    }

    public ClientEngine getEngine() {
        return engine;
    }


    public ClientPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ClientPlayer player) {
        this.player = player;
    }


    public void addPayload(ClientPayload object) {
        ((ClientPayload) object).setClientManager(this);
        synchronized (payloads) {
            payloads.add((ClientPayload) object);
        }
    }

    public void listStatus() {
        connection.listStatus();
        Console.println("Logged in : " + isLoggedIn());
    }

    public void connect(String address, int tcpPort, int udpPort) throws IOException {
        disconnect();
        connection.connect(address, tcpPort, udpPort);
    }

    public void disconnect() {
        loggedIn = false;
        player = null;
        connection.disconnect();
    }
}
