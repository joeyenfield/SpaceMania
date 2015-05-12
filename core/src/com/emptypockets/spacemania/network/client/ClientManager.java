package com.emptypockets.spacemania.network.client;

import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.command.CommandLine;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.CommandService;
import com.emptypockets.spacemania.network.client.engine.ClientEngine;
import com.emptypockets.spacemania.network.client.payloads.ClientPayload;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.payloads.authentication.LoginRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.authentication.LogoutRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.engine.PlayerStatePayload;
import com.emptypockets.spacemania.network.transport.NetworkProtocall;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;


public class ClientManager extends Listener implements Disposable{

    Client client;

    PlayerStatePayload state = new PlayerStatePayload();
    private CommandLine command;
    String username = "client";
    ServerManager serverManager;
    boolean loggedIn = false;

    ClientEngine engine;


    public ClientManager() {
        setCommand(new CommandLine());
        setupClient();
        NetworkProtocall.register(client.getKryo());
        CommandService.registerClient(this);
        engine = new ClientEngine();
    }

    public void setupClient() {
        client = new Client(10*1024*1024,10*1024*1024);
        client.start();
        client.addListener(this);
    }


    public void connect(String address, int tcpPort, int udpPort) throws IOException {
        loggedIn = false;
        Console.printf("Connecting to server %s : %d,%d\n", address, tcpPort, udpPort);
        client.connect(20000, address, tcpPort, udpPort);
    }

    public void listStatus() {
        Console.println("Connected : " + client.isConnected() + " - LoggedIn : " + loggedIn);
    }

    public void listNetworkServers(final int udpPort, final int timeoutSec, final NetworkDiscoveryInterface callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Console.println("Searching for hosts Port [" + udpPort + "] holding for [" + timeoutSec + " s]");
                List<InetAddress> hosts = client.discoverHosts(udpPort, timeoutSec * 1000);
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

    public void stop() {
        Console.println("Disconnecting from server");
        client.close();
    }

    public void startEngine() {
        engine.setStart();
    }

    public void stopEngine(){
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        if (object instanceof ClientPayload) {
            ((ClientPayload) object).setClientManager(this);
            ((ClientPayload) object).executePayload();
        }
    }


    public void serverLogin(String data) {
        Console.println("Sending Login Request to server");
        LoginRequestPayload request = new LoginRequestPayload();
        request.setUsername(data);
        client.sendTCP(request);
    }

    public void serverLogout() {
        Console.println("Sending Logout Request to server");
        LogoutRequestPayload request = new LogoutRequestPayload();
        client.sendTCP(request);
    }

    public void sendPlayerState(Touchpad movePad, Touchpad shootPad) {
        if(isLoggedIn()) {
            state.readPlayer(movePad,shootPad);
            client.sendTCP(state);
        }
    }


    public void dispose() {
        stop();
        if (serverManager != null) {
            serverManager.stop();
            serverManager.dispose();
        }
        serverManager = null;
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String data) {
        this.username = data;
    }

    public void setupServer(int updateCount) {
        serverManager = new ServerManager(updateCount);
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


}
