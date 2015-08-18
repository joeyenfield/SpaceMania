package com.emptypockets.spacemania.network.old.client;

import java.io.IOException;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.commandLine.CommandLine;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.input.ClientInputProducer;
import com.emptypockets.spacemania.metrics.plotter.DataLogger;
import com.emptypockets.spacemania.network.common.ComsType;
import com.emptypockets.spacemania.network.old.CommandService;
import com.emptypockets.spacemania.network.old.client.exceptions.ClientNotConnectedException;
import com.emptypockets.spacemania.network.old.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.old.client.player.MyPlayer;
import com.emptypockets.spacemania.network.old.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.old.engine.entities.Entity;
import com.emptypockets.spacemania.network.old.engine.entities.players.PlayerEntity;
import com.emptypockets.spacemania.network.old.server.payloads.ServerClientInputUpdatePayload;
import com.emptypockets.spacemania.network.old.server.payloads.authentication.LoginRequestPayload;
import com.emptypockets.spacemania.network.old.server.payloads.authentication.LogoutRequestPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.ChatMessagePayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.CreateRoomRequestPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.JoinLobyRequestPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.JoinRoomRequestPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.RequestRoomListPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.ResizeRoomPayload;
import com.emptypockets.spacemania.network.old.server.payloads.rooms.SpawnPlayerRequestPayload;
import com.emptypockets.spacemania.utils.PoolsManager;

public class ClientManager implements Disposable {
	CommandLine command;
	ClientConnectionManager connection;

	boolean loggedIn = false;
	MyPlayer player;
	ClientInputProducer inputProducer;
	ClientRoom currentRoom;
	ClientEngine engine;
	private Console console;

	public ClientManager(ClientInputProducer clientInputProducer) {
		setConsole(new Console("CLIENT : "));
		command = new CommandLine(getConsole());
		connection = new ClientConnectionManager(this);
		CommandService.registerClientCommands(this);
		this.inputProducer = clientInputProducer;
	}

	public void stop() {
		getConsole().println("Disconnecting from server");
		connection.disconnect();
	}

	public void update() {
		inputProducer.update();
		connection.processRecievedPayloads();
		// Update Room

		//Update your player with your movement
		if(player != null){
			int playerId = player.getEntityId();
			Entity ent = engine.getEntityManager().getEntityById(playerId);
			if(ent instanceof PlayerEntity){
				PlayerEntity player = (PlayerEntity)ent;
				player.applyClientInput(inputProducer.getInput());
				DataLogger.log("tmp-x",player.getVel().x);
			}
		}
		
		if (engine != null) {
			engine.update();
		}
		connection.processToSendPayloads();

	}

	public void serverLogin(String username, String password) {
		getConsole().println("Sending Login Request to server [" + username + "," + password + "]");
		LoginRequestPayload request = new LoginRequestPayload();
		request.setUsername(username);
		request.setPassword(password);
		try {
			connection.send(request, ComsType.TCP);
		} catch (ClientNotConnectedException e) {
			getConsole().println("Not connected");
		}
	}

	public void serverLogout() {
		getConsole().println("Sending Logout Request to server");
		LogoutRequestPayload request = new LogoutRequestPayload();
		try {
			connection.send(request, ComsType.TCP);
		} catch (ClientNotConnectedException e) {
			getConsole().println("Not connected");
		}
	}

	public void connect(String address, int tcpPort, int udpPort) throws IOException {
		getConsole().println("Connecting to [" + address + ":" + tcpPort + "," + udpPort + "]");
		disconnect();
		connection.connect(address, tcpPort, udpPort);
		engine = new ClientEngine();
	}

	public void disconnect() {
		getConsole().println("Disconnect");
		connection.disconnect();
	}

	public void postDisconnect() {
		loggedIn = false;
		player = null;
		currentRoom = null;
		engine = null;
	}

	public void listStatus() {
		getConsole().println("Listing Status");
		connection.listStatus();
		getConsole().println("Logged in : " + isLoggedIn());
		if (isLoggedIn()) {
			getConsole().println("Player : Username[" + getPlayer().getUsername() + "] Ping[" + getPlayer().getPing() + "]");
			getConsole().println("Room : " + (getCurrentRoom() == null ? "None" : getCurrentRoom().getName()));
		}
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
		if (player == null) {
			console.setConsoleKey("CLIENT[] : ");
		} else {
			console.setConsoleKey("CLIENT[" + player.getUsername() + "] : ");
		}
	}

	@Override
	public void dispose() {
		stop();
		connection.dispose();
		if (currentRoom != null) {
			currentRoom.dispose();
		}
		if (engine != null) {
			engine.dispose();
		}
	}

	public ClientRoom getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(ClientRoom currentRoom) {
		getConsole().println("Setting room : " + currentRoom.getName());
		this.currentRoom = currentRoom;
	}

	public void joinLobby() {
		engine.getEntityManager().clear();
		engine.start();
		getConsole().println("Joining :  Lobby");
		JoinLobyRequestPayload request = new JoinLobyRequestPayload();
		connection.send(request, ComsType.TCP);
	}

	public void joinRoom(String name) {
		engine.getEntityManager().clear();
		engine.start();
		getConsole().println("Joining Room : " + name);
		JoinRoomRequestPayload payload = new JoinRoomRequestPayload();
		payload.setRoomName(name);
		connection.send(payload, ComsType.TCP);
	}

	public void createRoom(String roomName) {
		getConsole().println("Creating Room : " + roomName);
		CreateRoomRequestPayload createRoom = PoolsManager.obtain(CreateRoomRequestPayload.class);
		createRoom.setRoomName(roomName);
		connection.send(createRoom, ComsType.TCP);
	}

	public void sendChatMessage(String message) {
		ChatMessagePayload payload = PoolsManager.obtain(ChatMessagePayload.class);
		payload.setMessage(message);
		connection.send(payload, ComsType.TCP);
	}

	public void requestServerRooms() {
		getConsole().println("Requesting Rooms");
		RequestRoomListPayload payload = PoolsManager.obtain(RequestRoomListPayload.class);
		connection.send(payload, ComsType.TCP);
	}

	public Console getConsole() {
		return console;
	}

	public void setConsole(Console console) {
		this.console = console;
	}

	public ClientEngine getEngine() {
		return engine;
	}

	public void sendInput() {
		ServerClientInputUpdatePayload payload = PoolsManager.obtain(ServerClientInputUpdatePayload.class);
		payload.setInput(inputProducer.getInput());
		connection.send(payload, ComsType.TCP);
	}

	public void resizeRoom(int size) {
		ResizeRoomPayload payload = PoolsManager.obtain(ResizeRoomPayload.class);
		payload.setRoomSize(size);
		connection.send(payload, ComsType.TCP);
	}

	public void spawn() {
		SpawnPlayerRequestPayload payload = PoolsManager.obtain(SpawnPlayerRequestPayload.class);
		connection.send(payload, ComsType.TCP);
	}

	public PlayerEntity getMyEntity() {
		if(engine != null){
			if(player != null){
				return (PlayerEntity) engine.getEntityManager().getEntityById(player.getEntityId());
			}
		}
		return null;
	}
}
