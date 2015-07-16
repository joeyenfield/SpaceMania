package com.emptypockets.spacemania.network.server;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.Constants;
import com.emptypockets.spacemania.commandLine.CommandLine;
import com.emptypockets.spacemania.console.Console;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.CommandService;
import com.emptypockets.spacemania.network.client.payloads.ClientMyPlayerStateUpdatePayload;
import com.emptypockets.spacemania.network.client.payloads.NotifyClientPayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginFailedResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LoginSuccessResponsePayload;
import com.emptypockets.spacemania.network.client.payloads.authentication.LogoutSuccessPayload;
import com.emptypockets.spacemania.network.client.payloads.rooms.JoinRoomSuccessPayload;
import com.emptypockets.spacemania.network.client.player.ClientPlayer;
import com.emptypockets.spacemania.network.server.exceptions.TooManyPlayersException;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.server.player.ServerPlayerManager;
import com.emptypockets.spacemania.network.server.rooms.ServerRoom;
import com.emptypockets.spacemania.network.server.rooms.ServerRoomManager;
import com.emptypockets.spacemania.network.transport.ComsType;
import com.emptypockets.spacemania.plotter.DataLogger;
import com.emptypockets.spacemania.utils.PoolsManager;
import com.esotericsoftware.kryo.Kryo;

public class ServerManager implements Disposable, Runnable {
	boolean alive = true;
	String name = "Server";
	int serverConnecedUsersCounter = 0;
	Console console;
	CommandLine command;

	ServerRoom lobbyRoom;
	ServerConnectionManager connectionManager;

	ServerPlayerManager playerManager;
	ServerRoomManager roomManager;

	Thread thread;

	long lastPingUpdate = 0;

	long lastplayerStateUpdate = 0;

	public ServerManager(Console console) {
		this.console = console;
		command = new CommandLine(console);
		playerManager = new ServerPlayerManager();
		connectionManager = new ServerConnectionManager(this);
		roomManager = new ServerRoomManager(this);
		lobbyRoom = roomManager.createNewRoom(null, "Lobby");
		CommandService.registerServerCommands(this);
	}

	public ServerManager() {
		this(new Console("SERVER : "));
	}

	public ServerRoomManager getRoomManager() {
		return roomManager;
	}

	public void start() throws IOException {
		console.println("Starting Server");
		// Startup the server
		connectionManager.start();
		alive = true;
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		console.println("Stopping Server");

		// Stop Server
		connectionManager.stop();
		alive = false;
	}

	public void resizeRoom(ServerRoom room, ServerPlayer serverPlayer, float size) {
		room.resizeRoom(size);
	}

	public void clientLogout(ClientConnection connection) {
		console.println("Client Logout : " + (connection.getPlayer() == null ? "None" : connection.getPlayer().getUsername()));
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

	protected synchronized ServerPlayer clientLogin(ClientConnection connection, String username, String password) throws TooManyPlayersException {
		console.println("Client Join : " + username);
		serverConnecedUsersCounter++;

		ServerPlayer player = new ServerPlayer(connection);
		player.setId(serverConnecedUsersCounter);
		player.setUsername(username);

		playerManager.addPlayer(player);
		connection.setPlayer(player);

		connection.updateReturnTripTime();
		return player;
	}

	public boolean isUserConnected(String userName) {
		ServerPlayer player = playerManager.getPlayerByUsername(userName);
		return player != null;
	}

	public void logStatus() {
		console.print("Server Running - Connected [" + connectionManager.getConnectedCount() + "]");
	}

	public void logUsers() {
		console.print("Connected [" + connectionManager.getConnectedCount() + "]" + " Players [" + playerManager.getPlayerCount() + "]");
		playerManager.process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer player) {
				console.print("Player [" + player.getUsername() + "](" + player.getPing() + ")ms - In Room[" + player.isInRoom() + "]");
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
			console.println("No user " + username);
		}
	}

	public void updatePings() {
		playerManager.process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer player) {
				try {
					player.updateReturnTripTime();
				} catch (Throwable t) {
					console.printf("Failed to update return trip time for player " + player.getUsername());
					console.error(t);
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

		long startTime = 0;
		long processingTime = 0;

		while (alive) {
			try {
				DataLogger.log("server-update", 1);
				startTime = System.currentTimeMillis();
				// Update All Pings
				long delta = System.currentTimeMillis() - lastPingUpdate;
				if (delta > Constants.SERVER_TIME_PING_UPDATE_PEROID) {
					lastPingUpdate = System.currentTimeMillis();
					updatePings();
				}
				// Read All Data
				connectionManager.processIncommingPackets();

				// Update Rooms and client information
				roomManager.process(new SingleProcessor<ServerRoom>() {
					@Override
					public void process(ServerRoom entity) {
						entity.update();
						if (entity.shouldBroadcast()) {
							entity.broadcast();
						}
					}
				});

				// Send all Player state
				delta = System.currentTimeMillis() - lastplayerStateUpdate;
				if (delta > Constants.SERVER_TIME_PLAYER_STATE_UPDATE_PEROID) {
					lastplayerStateUpdate = System.currentTimeMillis();
					broadcastPlayerStates();
				}

				processingTime = System.currentTimeMillis() - startTime;
				try {
					if (processingTime < Constants.SERVER_TIME_UPDATE_PEROID) {
						Thread.sleep(Constants.SERVER_TIME_UPDATE_PEROID - processingTime);
					} else {
						console.println("Server Running Behind : Update[" + processingTime + "] - Update Time [" + Constants.SERVER_TIME_UPDATE_PEROID + "]" + getLobbyRoom().getEngine().getEntityManager().getSize());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Throwable t) {
				getConsole().print("Error");
				getConsole().error(t);
			}
		}

	}

	private void broadcastPlayerStates() {
		// Update Server Player Client Information
		playerManager.process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer entity) {
				ClientMyPlayerStateUpdatePayload payload = PoolsManager.obtain(ClientMyPlayerStateUpdatePayload.class);
				payload.setMyPlayer(entity.getClientPlayer());
				entity.send(payload, ComsType.TCP);
				PoolsManager.free(payload);
			}
		});
	}

	public void closeRoom(ServerRoom room) {
		if (room != null && room.equals(getLobbyRoom())) {
			return;
		}
		console.println("Closing room : " + room.getName());
		final ArrayList<ServerPlayer> players = new ArrayList<ServerPlayer>();
		room.getPlayerManager().process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer entity) {
				NotifyClientPayload payload = PoolsManager.obtain(NotifyClientPayload.class);
				payload.setMessage("Room is closed - Returning to lobby.");
				entity.send(payload, ComsType.TCP);
				players.add(entity);
			}
		});

		for (ServerPlayer player : players) {
			joinRoom(player.getClientConnection(), lobbyRoom.getName());
		}
		roomManager.removeRoom(room);
	}

	public void joinRoom(ClientConnection clientConnection, String roomName) {
		if (clientConnection.isConnected() && clientConnection.isLoggedIn()) {
			ServerRoom newRoom = getRoomManager().findRoomByName(roomName);
			ServerPlayer player = clientConnection.getPlayer();
			ServerRoom currentRoom = player.getCurrentRoom();

			if (newRoom != null) {
				if (currentRoom != null && currentRoom.equals(newRoom)) {
					NotifyClientPayload payload = PoolsManager.obtain(NotifyClientPayload.class);
					payload.setMessage("You are already connected to this room");
					clientConnection.send(payload, ComsType.TCP);
					PoolsManager.free(payload);
				} else {
					try {

						// If already in a room leave it
						if (currentRoom != null) {
							currentRoom.leaveRoom(player);
							boolean closeRoom = false;
							if (currentRoom.getHost() != null && currentRoom.getHost().equals(player)) {
								closeRoom = true;
							}
							if (currentRoom.getPlayerCount() == 0 && !getLobbyRoom().equals(newRoom)) {
								closeRoom = true;
							}

							if (closeRoom) {
								closeRoom(currentRoom);
							}
						}
						newRoom.joinRoom(player);
						player.setCurrentRoom(newRoom);

						JoinRoomSuccessPayload payload = PoolsManager.obtain(JoinRoomSuccessPayload.class);
						payload.setRoom(newRoom.getClientRoom());
						payload.setInitialSync(player.getEntityManagerSync());
						clientConnection.send(payload, ComsType.TCP);
						player.getEntityManagerSync().clearAfterDataSend();

						PoolsManager.free(payload);

					} catch (TooManyPlayersException e) {
						NotifyClientPayload payload = PoolsManager.obtain(NotifyClientPayload.class);
						payload.setMessage("Could not connect to room as it was full");
						clientConnection.send(payload, ComsType.TCP);
						PoolsManager.free(payload);
					}
				}
			} else {
				NotifyClientPayload payload = PoolsManager.obtain(NotifyClientPayload.class);
				payload.setMessage("No room found with name [" + roomName + "]");
				clientConnection.send(payload, ComsType.TCP);
				PoolsManager.free(payload);
			}
		} else {
			NotifyClientPayload payload = PoolsManager.obtain(NotifyClientPayload.class);
			payload.setMessage("You are not logged in, please login first");
			clientConnection.send(payload, ComsType.TCP);
			PoolsManager.free(payload);
		}
	}

	public int getTcpPort() {
		return connectionManager.tcpPort;
	}

	public int getUdpPort() {
		return connectionManager.udpPort;
	}

	public int getConnectedCount() {
		return connectionManager.getConnectedCount();
	}

	public int getLoggedInCount() {
		return playerManager.getPlayerCount();
	}

	public int getRoomCount() {
		return roomManager.getSize();
	}

	public ServerRoom getRoomByName(String roomName) {
		return roomManager.findRoomByName(roomName);
	}

	public Console getConsole() {
		return console;
	}

	public void createRoom(ClientConnection clientConnection, String roomName) {
		if (clientConnection.isConnected()) {
			if (clientConnection.getPlayer() != null) {
				ServerRoom room = roomManager.createNewRoom(clientConnection.getPlayer(), roomName);
				joinRoom(clientConnection, roomName);
			} else {
				NotifyClientPayload payload = new NotifyClientPayload();
				payload.setMessage("You must be logged in to create a room");
				clientConnection.send(payload, ComsType.TCP);
			}
		} else {
			console.println("Room Creation failed");
		}

	}

	public void chatRecieved(ClientConnection clientConnection, String message) {
		if (clientConnection.isLoggedIn()) {
			if (clientConnection.getPlayer() != null && clientConnection.getPlayer().isInRoom()) {
				clientConnection.getPlayer().getCurrentRoom().sendMessage(message, clientConnection.getPlayer().getUsername());
			} else {
				NotifyClientPayload payload = new NotifyClientPayload();
				payload.setMessage("You are not in any room");
				clientConnection.send(payload, ComsType.TCP);
			}
		} else {
			NotifyClientPayload payload = new NotifyClientPayload();
			payload.setMessage("You must be logged in to chat");
			clientConnection.send(payload, ComsType.TCP);
		}
	}

	public void requestRoomList(ClientConnection clientConnection) {
		final StringBuilder roomList = new StringBuilder();
		roomList.append("Server Rooms \n");
		getRoomManager().process(new SingleProcessor<ServerRoom>() {
			@Override
			public void process(ServerRoom entity) {
				roomList.append(entity.getName() + " - [" + entity.getPlayerCount() + " / " + entity.getMaxPlayers() + "]\n");
			}
		});

		NotifyClientPayload payload = PoolsManager.obtain(NotifyClientPayload.class);
		payload.setMessage(roomList.toString());
		clientConnection.send(payload, ComsType.TCP);
		PoolsManager.free(payload);
	}

	public void login(ClientConnection clientConnection, String username, String password) {
		if (clientConnection.isLoggedIn()) {
			// The user is already logged in - Tell User to logout
			NotifyClientPayload resp = new NotifyClientPayload();
			resp.setMessage("You are already logged in as [" + clientConnection.getPlayer().getUsername() + "] - Log out first");
			clientConnection.sendTCP(resp);
		} else if (isUserConnected(username)) {
			// The username is already in use;
			console.println("User [" + username + "] is already connected");
			LoginFailedResponsePayload resp = new LoginFailedResponsePayload();
			resp.setErrorMessage("User already logged in");
			clientConnection.sendTCP(resp);
		} else {
			// Try to login
			try {
				ServerPlayer player = clientLogin(clientConnection, username, password);
				// Login user
				clientConnection.setPlayer(player);
				clientConnection.setLoggedIn(true);

				LoginSuccessResponsePayload resp = new LoginSuccessResponsePayload();
				resp.setUsername(player.getUsername());
				resp.setPlayerId(player.getId());
				clientConnection.sendTCP(resp);
			} catch (TooManyPlayersException e) {
				LoginFailedResponsePayload resp = new LoginFailedResponsePayload();
				resp.setErrorMessage("Server currently full");
				clientConnection.sendTCP(resp);
			}
		}

	}

	public void logout(ClientConnection clientConnection) {
		if (clientConnection.isLoggedIn()) {
			clientLogout(clientConnection);
			clientConnection.setPlayer(null);
			clientConnection.setLoggedIn(false);
			LogoutSuccessPayload response = new LogoutSuccessPayload();
			clientConnection.sendTCP(response);
		} else {
			NotifyClientPayload resp = new NotifyClientPayload();
			resp.setMessage("Your not logged in");
			clientConnection.sendTCP(resp);
		}
	}

	public Kryo getKryo() {
		return connectionManager.getKryo();

	}

	public boolean ensureLoggedIn(ClientConnection clientConnection) {
		if (clientConnection.isLoggedIn()) {
			return true;
		} else {
			NotifyClientPayload payload = new NotifyClientPayload();
			payload.setMessage("You are not logged in");
			clientConnection.send(payload, ComsType.TCP);
		}
		return false;
	}

	public boolean ensureInRoom(ClientConnection clientConnection) {
		if (ensureLoggedIn(clientConnection)) {
			if (clientConnection.getPlayer() != null && clientConnection.getPlayer().isInRoom()) {
				return true;
			} else {
				NotifyClientPayload payload = new NotifyClientPayload();
				payload.setMessage("You are not in any room");
				clientConnection.send(payload, ComsType.TCP);
			}
		}
		return false;
	}

	public void requestResizeRoom(ClientConnection clientConnection, float size) {
		if (ensureInRoom(clientConnection)) {
			ServerRoom room = clientConnection.getPlayer().getCurrentRoom();
			resizeRoom(room, clientConnection.getPlayer(), size);
		}
	}

	public void spawnPlayer(ClientConnection clientConnection) {
		if(ensureInRoom(clientConnection)){
			ServerRoom room = clientConnection.getPlayer().getCurrentRoom();
			room.spawnPlayer(clientConnection.getPlayer());
		}
	}

}