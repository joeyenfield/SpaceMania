package com.emptypockets.spacemania.network.server.rooms;

import com.badlogic.gdx.utils.Disposable;
import com.emptypockets.spacemania.holders.ArrayListProcessor;
import com.emptypockets.spacemania.holders.CleanerProcessor;
import com.emptypockets.spacemania.holders.SingleProcessor;
import com.emptypockets.spacemania.network.client.payloads.rooms.ClientRoomMessagesPayload;
import com.emptypockets.spacemania.network.client.rooms.ClientRoom;
import com.emptypockets.spacemania.network.engine.EngineState;
import com.emptypockets.spacemania.network.engine.entities.Entity;
import com.emptypockets.spacemania.network.engine.entities.EntityType;
import com.emptypockets.spacemania.network.engine.sync.EntityManagerSync;
import com.emptypockets.spacemania.network.server.ServerManager;
import com.emptypockets.spacemania.network.server.engine.ServerEngine;
import com.emptypockets.spacemania.network.server.exceptions.TooManyPlayersException;
import com.emptypockets.spacemania.network.server.player.PlayerManager;
import com.emptypockets.spacemania.network.server.player.ServerPlayer;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomChatMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomPlayerJoinMessage;
import com.emptypockets.spacemania.network.server.rooms.messages.ServerRoomPlayerLeaveMessage;
import com.emptypockets.spacemania.network.transport.ComsType;
import com.emptypockets.spacemania.utils.PoolsManager;

/**
 * Created by jenfield on 14/05/2015.
 */
public class ServerRoom implements Disposable {
	int id;
	String name;
	ServerManager manager;
	ServerPlayer host;
	ArrayListProcessor<ServerRoomMessage> messageManager;
	PlayerManager playerManager;
	ClientRoom clientRoom;
	ServerEngine engine;
	EngineState engineState;
	boolean sendEngineState = false;
	long lastBroadcastTime = 0;
	long broadcastPeroid = 100;

	public ServerRoom(ServerManager manager) {
		super();
		this.manager = manager;
		messageManager = new ArrayListProcessor<ServerRoomMessage>();
		playerManager = new PlayerManager();
		clientRoom = new ClientRoom();
		engine = new ServerEngine(playerManager);
		engineState = new EngineState();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof ServerPlayer) {
			return ((ServerPlayer) o).getId() == getId();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getId();
	}

	public int getMaxPlayers() {
		return playerManager.getMaxPlayers();
	}

	public void setMaxPlayers(int maxPlayers) {
		playerManager.setMaxPlayers(maxPlayers);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isEmpty() {
		return playerManager.isEmpty();
	}

	@Override
	public void dispose() {
		playerManager.clear();
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public int getPlayerCount() {
		return playerManager.getSize();
	}

	public ClientRoom getClientRoom() {
		return clientRoom;
	}

	public void processPlayerInput() {
		playerManager.process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer player) {
				player.processInput(engine);
			}
		});
	}

	public void update() {
		processPlayerInput();
		engine.update();
		clientRoom.read(this);
	}

	public synchronized void broadcast() {
		lastBroadcastTime = System.currentTimeMillis();
		// If no messages dont broadcast
		if (messageManager.getSize() > 0) {
			final ClientRoomMessagesPayload roomMessagePayloads = PoolsManager.obtain(ClientRoomMessagesPayload.class);
			roomMessagePayloads.setRoomId(getId());
			messageManager.process(new CleanerProcessor<ServerRoomMessage>() {
				@Override
				public boolean shouldRemove(ServerRoomMessage entity) {
					roomMessagePayloads.addMessage(entity.createClientMessage());
					PoolsManager.free(entity);
					return true;
				}
			});
			getPlayerManager().process(new SingleProcessor<ServerPlayer>() {
				@Override
				public void process(ServerPlayer entity) {
					entity.send(roomMessagePayloads, ComsType.TCP);
				}
			});
		}

		if (sendEngineState) {
			engineState.readFrom(engine);
			// Broadcast Player Entity Managers
			playerManager.process(new SingleProcessor<ServerPlayer>() {
				@Override
				public void process(ServerPlayer player) {
					engineState.broadcast(player);
				}
			});

			sendEngineState = false;
		}

		// Broadcast Player Entity Managers
		playerManager.process(new SingleProcessor<ServerPlayer>() {
			@Override
			public void process(ServerPlayer player) {
				EntityManagerSync sync = player.getEntityManagerSync();
				sync.setTime(engine.getEngineLastUpdateTime());
				sync.setSyncTime(false);
				sync.broadcast(player);
			}
		});
	}

	public synchronized void joinRoom(ServerPlayer player) throws TooManyPlayersException {
		playerManager.addPlayer(player);

		// Add entity for player
		Entity entity = engine.getEntityManager().createEntity(EntityType.Player);
		entity.getState().getPos().x = 5;
		entity.getState().getPos().y = 5;
		engine.getEntityManager().addEntity(entity);
		player.setEntityId(entity.getState().getId());

		// Send message that player has joined
		ServerRoomPlayerJoinMessage message = PoolsManager.obtain(ServerRoomPlayerJoinMessage.class);
		message.setServerPlayer(player);
		messageManager.add(message);
		sendMessage(String.format("%s has joined the room", player.getUsername()));

		engine.getEntityManager().register(player.getEntityManagerSync());
		player.getEntityManagerSync().setTime(engine.getEngineLastUpdateTime());
	}

	public void leaveRoom(ServerPlayer player) {
		playerManager.removePlayer(player);

		ServerRoomPlayerLeaveMessage message = PoolsManager.obtain(ServerRoomPlayerLeaveMessage.class);
		message.setServerPlayer(player);
		messageManager.add(message);

		engine.getEntityManager().removeEntityById(player.getEntityId(), true);
		engine.getEntityManager().unregister(player.getEntityManagerSync());
		player.getEntityManagerSync().reset();

		sendMessage(String.format("%s has left the room", player.getUsername()));
	}

	public void sendMessage(String messageContent) {
		sendMessage(messageContent, null);
	}

	public void sendMessage(String messageContent, String username) {
		ServerRoomChatMessage message = PoolsManager.obtain(ServerRoomChatMessage.class);
		message.setTimestamp(System.currentTimeMillis());
		message.setMessage(messageContent);
		message.setUsername(username);
		messageManager.add(message);
	}

	public void resizeRoom(float size) {
		engine.setRegion(size);
		sendEngineState = true;
	}

	public ServerPlayer getHost() {
		return host;
	}

	public void setHost(ServerPlayer host) {
		this.host = host;
	}

	public ServerEngine getEngine() {
		return engine;
	}

	public long getLastBroadcastTime() {
		return lastBroadcastTime;
	}

	public long getBroadcastPeroid() {
		return broadcastPeroid;
	}

	public void setBroadcastPeroid(long broadcastPeroid) {
		this.broadcastPeroid = broadcastPeroid;
	}

	public boolean shouldBroadcast() {
		return System.currentTimeMillis() - lastBroadcastTime > broadcastPeroid;
	}

}
