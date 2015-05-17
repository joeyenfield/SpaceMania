package com.emptypockets.spacemania.network.client;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pools;
import com.emptypockets.spacemania.commandLine.CommandLine;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.network.CommandService;
import com.emptypockets.spacemania.network.client.exceptions.ClientNotConnectedException;
import com.emptypockets.spacemania.network.client.player.MyPlayer;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.server.payloads.authentication.LoginRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.authentication.LogoutRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.ChatMessagePayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.CreateRoomRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.JoinLobyRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.JoinRoomRequestPayload;
import com.emptypockets.spacemania.network.server.payloads.rooms.RequestRoomListPayload;

import java.io.IOException;


public class ClientManager implements Disposable {
    CommandLine command;
    ClientConnectionManager connection;

    boolean loggedIn = false;
    MyPlayer player;
    ClientRoom currentRoom;
    private Object serverRooms;

    public ClientManager() {
        command = new CommandLine();
        connection = new ClientConnectionManager(this);
        CommandService.registerClientCommands(this);
    }

    public void stop() {
        Console.println("Disconnecting from server");
        connection.disconnect();
    }

    public void update() {
        connection.processRecievedPayloads();
        //Update Room
        if (currentRoom != null) {
            currentRoom.update();
        }
        connection.processToSendPayloads();
    }


    public void serverLogin(String username, String password) {
        Console.println("Sending Login Request to server");
        LoginRequestPayload request = new LoginRequestPayload();
        request.setUsername(username);
        request.setPassword(password);
        try {
            connection.send(request);
        } catch (ClientNotConnectedException e) {
            Console.println("Not connected");
        }
    }

    public void serverLogout() {
        Console.println("Sending Logout Request to server");
        LogoutRequestPayload request = new LogoutRequestPayload();
        try {
            connection.send(request);
        } catch (ClientNotConnectedException e) {
            Console.println("Not connected");
        }
    }

    public void connect(String address, int tcpPort, int udpPort) throws IOException {
        disconnect();
        connection.connect(address, tcpPort, udpPort);
    }

    public void disconnect() {
        connection.disconnect();
    }

    public void postDisconnect() {
        loggedIn = false;
        player = null;
        currentRoom = null;
    }

    public void listStatus() {
        connection.listStatus();
        Console.println("Logged in : " + isLoggedIn());
        Console.println("Room : " + (getCurrentRoom() == null ? "None" : getCurrentRoom().getName()));
    }

    public void updatePing() {
        if (connection.isConnected()) {
            connection.updatePing();
        }
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

    public MyPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MyPlayer player) {
        this.player = player;
    }

    @Override
    public void dispose() {
        stop();
        connection.dispose();
        if (currentRoom == null) {
            currentRoom.dispose();
        }
    }

    public ClientRoom getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(ClientRoom currentRoom) {
        Console.println("Joining room " + currentRoom.getName());
        this.currentRoom = currentRoom;
    }

    public void joinLobby() {
        JoinLobyRequestPayload request = new JoinLobyRequestPayload();
        connection.send(request);
    }

    public void joinRoom(String name) {
        JoinRoomRequestPayload payload = new JoinRoomRequestPayload();
        payload.setRoomName(name);
        connection.send(payload);
    }

    public void createRoom(String roomName) {
        CreateRoomRequestPayload createRoom = Pools.obtain(CreateRoomRequestPayload.class);
        createRoom.setRoomName(roomName);
        connection.send(createRoom);
    }

    public void sendChatMessage(String message) {
        ChatMessagePayload payload = Pools.obtain(ChatMessagePayload.class);
        payload.setMessage(message);
        connection.send(payload);
    }

    public void requestServerRooms() {
        RequestRoomListPayload payload = Pools.obtain(RequestRoomListPayload.class);
        connection.send(payload);
    }
}
